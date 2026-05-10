package com.lilamaris.capstone.identity.auth.security.credential.config;

import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.RegisterCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.security.credential.filter.JsonCredentialSignInProcessingFilter;
import com.lilamaris.capstone.identity.auth.security.credential.filter.JsonCredentialSignUpProcessingFilter;
import com.lilamaris.capstone.identity.auth.security.credential.handler.CredentialAuthenticationFailureHandler;
import com.lilamaris.capstone.identity.auth.security.credential.handler.CredentialAuthenticationSuccessHandler;
import com.lilamaris.capstone.identity.auth.security.credential.provider.CredentialSignInProvider;
import com.lilamaris.capstone.identity.auth.security.credential.provider.CredentialSignUpProvider;
import com.lilamaris.capstone.identity.auth.security.shared.config.IdentityAuthSecurityConfiguration;
import com.lilamaris.capstone.identity.auth.security.shared.response.ResponseWriter;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.NamespaceRoleSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Configuration
public class CredentialSecurityConfiguration {
    @Bean
    @Order(2)
    SecurityFilterChain credentialAuthenticationSecurityFilterChain(
            HttpSecurity httpSecurity,
            @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigurationSource,
            JsonCredentialSignInProcessingFilter jsonCredentialSignInProcessingFilter,
            JsonCredentialSignUpProcessingFilter jsonCredentialSignUpProcessingFilter
    ) {
        IdentityAuthSecurityConfiguration.withDefaultFilter(httpSecurity, corsConfigurationSource);

        httpSecurity
                .securityMatcher("/auth/**")
                .authorizeHttpRequests(
                        auth -> auth
                                .anyRequest().permitAll()
                )
                .addFilterBefore(jsonCredentialSignInProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jsonCredentialSignUpProcessingFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    JsonCredentialSignInProcessingFilter jsonCredentialSignInProcessingFilter(
            @Qualifier("credential") AuthenticationManager authenticationManager,
            CredentialAuthenticationSuccessHandler successHandler,
            CredentialAuthenticationFailureHandler failureHandler,
            ObjectMapper objectMapper
    ) {
        var matcher = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/auth/sign-in");
        var filter = new JsonCredentialSignInProcessingFilter(matcher, objectMapper);

        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);

        return filter;
    }

    @Bean
    JsonCredentialSignUpProcessingFilter jsonCredentialSignUpProcessingFilter(
            @Qualifier("credential") AuthenticationManager authenticationManager,
            CredentialAuthenticationSuccessHandler successHandler,
            CredentialAuthenticationFailureHandler failureHandler,
            ObjectMapper objectMapper
    ) {
        var matcher = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/auth/sign-up");
        var filter = new JsonCredentialSignUpProcessingFilter(matcher, objectMapper);

        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);

        return filter;
    }

    @Bean
    CredentialSignInProvider credentialSignInProvider(AuthenticateCredentialAccountUseCase authenticateCredentialAccountUseCase) {
        return new CredentialSignInProvider(authenticateCredentialAccountUseCase);
    }

    @Bean
    CredentialSignUpProvider credentialSignUpProvider(RegisterCredentialAccountUseCase registerCredentialAccountUseCase) {
        return new CredentialSignUpProvider(registerCredentialAccountUseCase);
    }

    @Bean
    CredentialAuthenticationSuccessHandler credentialAuthenticationSuccessHandler(
            TokenResponseProcessor tokenResponseProcessor,
            NamespaceRoleSerializer namespaceRoleSerializer
    ) {
        return new CredentialAuthenticationSuccessHandler(
                tokenResponseProcessor,
                namespaceRoleSerializer
        );
    }

    @Bean
    CredentialAuthenticationFailureHandler credentialAuthenticationFailureHandler(ResponseWriter responseWriter) {
        return new CredentialAuthenticationFailureHandler(responseWriter);
    }

    @Bean
    @Qualifier("credential")
    AuthenticationManager credentialAuthenticationManager(
            CredentialSignUpProvider credentialSignUpProvider,
            CredentialSignInProvider credentialSignInProvider
    ) {
        return new ProviderManager(List.of(
                credentialSignInProvider,
                credentialSignUpProvider
        ));
    }
}
