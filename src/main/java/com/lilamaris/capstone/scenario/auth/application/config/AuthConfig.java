package com.lilamaris.capstone.scenario.auth.application.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.Duration;

@Configuration
public class AuthConfig {
    @Bean
    Duration accessTokenExpiration(
            @Value("${spring.security.jwt.accessTokenExpiration}")
            Duration expiration
    ) {
        return expiration;
    }

    @Bean
    SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    SecretKey secretKey(
            @Value("${spring.security.jwt.secret}") String secret
    ) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
