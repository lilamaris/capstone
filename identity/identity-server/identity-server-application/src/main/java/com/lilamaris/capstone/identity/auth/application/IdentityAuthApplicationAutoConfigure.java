package com.lilamaris.capstone.identity.auth.application;

import com.lilamaris.capstone.identity.auth.application.jwks.port.out.JwksReader;
import com.lilamaris.capstone.identity.auth.application.jwks.properties.IssueJwtProperties;
import com.lilamaris.capstone.identity.core.actor.context.ActorContextHolder;
import com.lilamaris.capstone.identity.core.actor.context.ThreadLocalActorContextHolder;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.identity.core.role.InitialUserGrantedRoleProvider;
import com.lilamaris.capstone.identity.core.role.InitialUserGrantedRoleRegistry;
import com.lilamaris.capstone.identity.core.role.ProviderBasedInitialUserGrantedRoleRegistry;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.ServiceLoader;
import java.util.random.RandomGenerator;

@AutoConfiguration
@EnableConfigurationProperties(IssueJwtProperties.class)
public class IdentityAuthApplicationAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(InitialUserGrantedRoleRegistry.class)
    InitialUserGrantedRoleRegistry initialUserGrantedRoleRegistry() {
        var providers = ServiceLoader.load(InitialUserGrantedRoleProvider.class).stream()
                .map(ServiceLoader.Provider::get)
                .toList();

        return new ProviderBasedInitialUserGrantedRoleRegistry(providers, CanonicalRole.GUEST);
    }

    @Bean
    @ConditionalOnMissingBean(RandomGenerator.class)
    RandomGenerator randomGenerator() {
        return new SecureRandom();
    }

    @Bean
    @ConditionalOnBean(JwksReader.class)
    @ConditionalOnMissingBean(JwtEncoder.class)
    JwtEncoder jwtEncoder(JwksReader jwksReader) {
        var signableKey = jwksReader.findSignableKey();
        var rsaKey = new RSAKey.Builder(signableKey.publicKey())
                .privateKey(signableKey.privateKey())
                .keyID(signableKey.kid())
                .build();
        JWKSource<SecurityContext> source = (selector, context) -> selector.select(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(source);
    }
}
