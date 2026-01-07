package com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.filter.JsonCredentialRegisterProcessingFilter;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.filter.JsonCredentialSignInProcessingFilter;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.filter.RefreshTokenProcessingFilter;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.provider.CredentialRegisterProvider;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.provider.CredentialSignInProvider;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.provider.RefreshTokenProvider;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.handler.CustomAuthenticationFailureHandler;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.handler.CustomAuthenticationSuccessHandler;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.oidc.CustomOAuth2UserService;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.oidc.CustomOidcUserService;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.oidc.handler.CustomOidcSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class AuthenticationConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    @Order(1)
    SecurityFilterChain oidcAuthenticationSecurityFilterChain(
            HttpSecurity http,
            CustomOidcUserService customOidcUserService,
            CustomOAuth2UserService customOAuth2UserService,
            CustomOidcSuccessHandler customOidcSuccessHandler
    ) throws Exception {
        configureCommonChain(http);

        http
                .securityMatcher("/oauth2/**", "/login/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(
                                u -> u
                                        .oidcUserService(customOidcUserService)
                                        .userService(customOAuth2UserService)
                        )
                        .successHandler(customOidcSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                );

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain credentialAuthenticationSecurityFilterChain(
            HttpSecurity http,
            ObjectMapper mapper,
            AuthenticationManager credentialAuthenticationManager
    ) throws Exception {
        configureCommonChain(http);

        var registerFilter = new JsonCredentialRegisterProcessingFilter(mapper);
        registerFilter.setAuthenticationManager(credentialAuthenticationManager);
        registerFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        registerFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        var signInFilter = new JsonCredentialSignInProcessingFilter(mapper);
        signInFilter.setAuthenticationManager(credentialAuthenticationManager);
        signInFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        signInFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        var refreshFilter = new RefreshTokenProcessingFilter();
        refreshFilter.setAuthenticationManager(credentialAuthenticationManager);
        refreshFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        refreshFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        http
                .securityMatcher("/api/v1/auth/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                .addFilterBefore(registerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(signInFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(refreshFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    AuthenticationManager credentialAuthenticationManager(
            CredentialRegisterProvider credentialRegisterProvider,
            CredentialSignInProvider credentialSignInProvider,
            RefreshTokenProvider refreshTokenProvider
    ) {
        return new ProviderManager(
                List.of(credentialRegisterProvider, credentialSignInProvider, refreshTokenProvider)
        );
    }

    private void configureCommonChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource));
    }
}
