package com.lilamaris.capstone.refresh_token.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RefreshTokenConfig {
    @Bean
    Duration refreshTokenExpiration(
            @Value("${spring.security.jwt.refreshTokenExpiration}")
            Duration expiration
    ) {
        return expiration;
    }
}
