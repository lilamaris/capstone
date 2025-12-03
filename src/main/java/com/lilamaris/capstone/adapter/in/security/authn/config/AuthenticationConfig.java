package com.lilamaris.capstone.adapter.in.security.authn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.capstone.adapter.in.security.authn.credential.provider.CredentialAuthenticationProvider;
import com.lilamaris.capstone.adapter.in.security.authn.credential.filter.JsonCredentialRegisterProcessingFilter;
import com.lilamaris.capstone.adapter.in.security.authn.credential.filter.JsonCredentialSignInProcessingFilter;
import com.lilamaris.capstone.adapter.in.security.authn.credential.provider.CredentialRegisterProvider;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.CustomOidcUserService;
import com.lilamaris.capstone.adapter.in.security.authn.oidc.OidcSuccessHandler;
import com.lilamaris.capstone.adapter.in.security.authz.jwt.JwtAccessDeniedHandler;
import com.lilamaris.capstone.adapter.in.security.authz.jwt.JwtAuthenticationEntryPoint;
import com.lilamaris.capstone.adapter.in.security.authz.jwt.JwtAuthenticationFilter;
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
    private final ObjectMapper mapper;

    private final CustomOidcUserService customOidcUserService;
    private final OidcSuccessHandler oidcSuccessHandler;
    private final CredentialAuthenticationProvider credentialAuthenticationProvider;
    private final CredentialRegisterProvider credentialRegisterProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(credentialAuthenticationProvider, credentialRegisterProvider));
    }

    @Bean
    JsonCredentialSignInProcessingFilter jsonCredentialSignInProcessingFilter() {
        var filter = new JsonCredentialSignInProcessingFilter(mapper);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    JsonCredentialRegisterProcessingFilter jsonCredentialRegisterProcessingFilter() {
        var filter = new JsonCredentialRegisterProcessingFilter(mapper);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    @Order(1)
    SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/auth/**", "/oauth2/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jsonCredentialRegisterProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jsonCredentialSignInProcessingFilter(), UsernamePasswordAuthenticationFilter.class)

                .authenticationManager(authenticationManager())

                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(u -> u.oidcUserService(customOidcUserService))
                        .successHandler(oidcSuccessHandler))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource));

        return http.build();
    }
}
