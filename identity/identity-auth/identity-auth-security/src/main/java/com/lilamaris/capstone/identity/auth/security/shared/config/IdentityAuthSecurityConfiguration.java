package com.lilamaris.capstone.identity.auth.security.shared.config;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueJwtUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueOpaqueTokenUseCase;
import com.lilamaris.capstone.identity.auth.security.shared.response.ResponseWriter;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.SeparatorBasedNamespaceRoleSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(IdentityAuthSecurityProperties.class)
public class IdentityAuthSecurityConfiguration {

    public static void withDefaultFilter(
            HttpSecurity httpSecurity,
            CorsConfigurationSource corsConfigurationSource
    ) {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource));
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(
            IdentityAuthSecurityProperties properties
    ) {
        var configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(properties.allowedOrigins());
        configuration.setAllowedMethods(properties.allowedMethods());
        configuration.setAllowedHeaders(properties.allowedHeaders());
        configuration.setAllowCredentials(properties.allowCredentials());
        configuration.setExposedHeaders(properties.exposedHeaders());

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    TokenResponseProcessor tokenResponseProcessor(
            IssueJwtUseCase issueJwtUseCase,
            IssueOpaqueTokenUseCase issueOpaqueTokenUseCase,
            ResponseWriter responseWriter
    ) {
        return new TokenResponseProcessor(issueJwtUseCase, issueOpaqueTokenUseCase, responseWriter);
    }

    @Bean
    ResponseWriter responseWriter(ObjectMapper objectMapper) {
        return new ResponseWriter(objectMapper);
    }

    @Bean
    SeparatorBasedNamespaceRoleSerializer namespaceRoleSerializer() {
        return new SeparatorBasedNamespaceRoleSerializer();
    }
}
