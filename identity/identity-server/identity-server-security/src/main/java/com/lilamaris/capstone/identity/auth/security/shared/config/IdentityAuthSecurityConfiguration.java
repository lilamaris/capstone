package com.lilamaris.capstone.identity.auth.security.shared.config;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueJwtUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueOpaqueTokenUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.port.out.JwksReader;
import com.lilamaris.capstone.identity.auth.contract.IdentityAuthNamespace;
import com.lilamaris.capstone.identity.auth.security.shared.response.ResponseWriter;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.SeparatorBasedNamespaceRoleSerializer;
import com.lilamaris.capstone.kernel.core.namespace.RunningNamespaceContext;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class IdentityAuthSecurityConfiguration {
    @Bean
    @ConditionalOnMissingBean(RunningNamespaceContext.class)
    RunningNamespaceContext runningNamespaceContext() {
        return () -> IdentityAuthNamespace.NAMESPACE;
    }

    @Bean
    @ConditionalOnBean(JwksReader.class)
    @ConditionalOnMissingBean(JwtDecoder.class)
    JwtDecoder jwtDecoder(JwksReader jwksReader) {
        JWKSource<SecurityContext> source = (selector, context) -> {
            var keys = jwksReader.findVerifiableKeys().stream()
                    .map(key -> new RSAKey.Builder(key.getPublicKey())
                            .keyID(key.kid())
                            .keyUse(KeyUse.SIGNATURE)
                            .algorithm(JWSAlgorithm.RS256)
                            .build())
                    .map(JWK.class::cast)
                    .toList();

            return selector.select(new JWKSet(keys));
        };

        return NimbusJwtDecoder.withJwkSource(source).build();
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
