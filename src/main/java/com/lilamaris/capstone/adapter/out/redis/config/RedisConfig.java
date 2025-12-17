package com.lilamaris.capstone.adapter.out.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.capstone.adapter.out.redis.store.RefreshTokenEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<String, RefreshTokenEntry> refreshTokenTemplate(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper
    ) {
        var serializer = new Jackson2JsonRedisSerializer<>(objectMapper, RefreshTokenEntry.class);

        var template = new RedisTemplate<String, RefreshTokenEntry>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }
}
