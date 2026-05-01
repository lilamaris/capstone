package com.lilamaris.capstone.identity.auth.security.federated.config;

import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateFederatedAccountUseCase;
import com.lilamaris.capstone.identity.auth.security.federated.handler.FederatedAuthenticationFailureHandler;
import com.lilamaris.capstone.identity.auth.security.federated.handler.FederatedAuthenticationSuccessHandler;
import com.lilamaris.capstone.identity.auth.security.federated.registry.FederatedPrincipalMapperRegistry;
import com.lilamaris.capstone.identity.auth.security.federated.service.CustomOAuth2UserService;
import com.lilamaris.capstone.identity.auth.security.federated.service.CustomOidcUserService;
import com.lilamaris.capstone.identity.auth.security.shared.response.ResponseWriter;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.NamespaceRoleSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;

@Configuration
public class FederatedSecurityConfiguration {
    @Bean
    Customizer<OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig> userInfoEndpointConfigCustomizer(
            CustomOidcUserService oidcUserService,
            CustomOAuth2UserService oAuth2UserService
    ) {
        return userInfoEndpointConfig -> userInfoEndpointConfig
                .oidcUserService(oidcUserService)
                .userService(oAuth2UserService);
    }

    @Bean
    Customizer<OAuth2LoginConfigurer<HttpSecurity>> oAuth2LoginConfigurerCustomizer(
            Customizer<OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig> userInfoEndpointConfigCustomizer,
            FederatedAuthenticationSuccessHandler successHandler,
            FederatedAuthenticationFailureHandler failureHandler
    ) {
        return auth -> auth
                .userInfoEndpoint(userInfoEndpointConfigCustomizer)
                .successHandler(successHandler)
                .failureHandler(failureHandler);
    }

    @Bean
    FederatedAuthenticationSuccessHandler successHandler(
            AuthenticateFederatedAccountUseCase authenticateFederatedAccountUseCase,
            NamespaceRoleSerializer namespaceRoleSerializer,
            TokenResponseProcessor tokenResponseProcessor
    ) {
        return new FederatedAuthenticationSuccessHandler(
                authenticateFederatedAccountUseCase,
                namespaceRoleSerializer,
                tokenResponseProcessor
        );
    }

    @Bean
    FederatedAuthenticationFailureHandler failureHandler(ResponseWriter responseWriter) {
        return new FederatedAuthenticationFailureHandler(responseWriter);
    }

    @Bean
    CustomOidcUserService oidcUserService(FederatedPrincipalMapperRegistry registry) {
        return new CustomOidcUserService(registry);
    }

    @Bean
    CustomOAuth2UserService oAuth2UserService(FederatedPrincipalMapperRegistry registry) {
        return new CustomOAuth2UserService(registry);
    }
}
