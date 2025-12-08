package com.lilamaris.capstone.adapter.in.security.authn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.capstone.adapter.in.security.authn.credential.handler.CredentialFailureHandler;
import com.lilamaris.capstone.adapter.in.security.authn.credential.handler.CredentialSuccessHandler;
import com.lilamaris.capstone.adapter.in.security.authn.credential.provider.CredentialAuthenticationProvider;
import com.lilamaris.capstone.adapter.in.security.authn.credential.filter.JsonCredentialRegisterProcessingFilter;
import com.lilamaris.capstone.adapter.in.security.authn.credential.filter.JsonCredentialSignInProcessingFilter;
import com.lilamaris.capstone.adapter.in.security.authn.credential.provider.CredentialRegisterProvider;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.CustomOidcUserService;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.handler.OidcFailureHandler;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.handler.OidcSuccessHandler;
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

    @Bean
    @Order(1)
    SecurityFilterChain oidcAuthenticationSecurityFilterChain(
            HttpSecurity http,
            CustomOidcUserService customOidcUserService,
            OidcSuccessHandler oidcSuccessHandler,
            OidcFailureHandler oidcFailureHandler
    ) throws Exception {
        configureCommonChain(http);

        http
                .securityMatcher("/oauth2/**", "/login/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(u -> u.oidcUserService(customOidcUserService))
                        .successHandler(oidcSuccessHandler)
                        .failureHandler(oidcFailureHandler)
                );

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain credentialAuthenticationSecurityFilterChain(
            HttpSecurity http,
            ObjectMapper mapper,
            AuthenticationManager credentialAuthenticationManager,
            CredentialSuccessHandler credentialSuccessHandler,
            CredentialFailureHandler credentialFailureHandler
    ) throws Exception {
        configureCommonChain(http);

        JsonCredentialRegisterProcessingFilter jsonCredentialRegisterProcessingFilter =
                new JsonCredentialRegisterProcessingFilter(mapper);
        jsonCredentialRegisterProcessingFilter.setAuthenticationManager(credentialAuthenticationManager);
        jsonCredentialRegisterProcessingFilter.setAuthenticationSuccessHandler(credentialSuccessHandler);
        jsonCredentialRegisterProcessingFilter.setAuthenticationFailureHandler(credentialFailureHandler);

        JsonCredentialSignInProcessingFilter jsonCredentialSignInProcessingFilter =
                new JsonCredentialSignInProcessingFilter(mapper);
        jsonCredentialSignInProcessingFilter.setAuthenticationManager(credentialAuthenticationManager);
        jsonCredentialSignInProcessingFilter.setAuthenticationSuccessHandler(credentialSuccessHandler);
        jsonCredentialSignInProcessingFilter.setAuthenticationFailureHandler(credentialFailureHandler);

        http
                .securityMatcher("/api/v1/auth/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                .addFilterBefore(jsonCredentialRegisterProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jsonCredentialSignInProcessingFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    AuthenticationManager credentialAuthenticationManager(
            CredentialRegisterProvider credentialRegisterProvider,
            CredentialAuthenticationProvider credentialAuthenticationProvider
    ) {
        return new ProviderManager(
                List.of(credentialRegisterProvider, credentialAuthenticationProvider)
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
