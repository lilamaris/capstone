package com.lilamaris.capstone.refresh_token.infrastructure.persistence.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.capstone.refresh_token.domain.RefreshToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<String, RefreshToken> refreshTokenTemplate(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper
    ) {
        var serializer = new Jackson2JsonRedisSerializer<>(objectMapper, RefreshToken.class);

        var template = new RedisTemplate<String, RefreshToken>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }
}
