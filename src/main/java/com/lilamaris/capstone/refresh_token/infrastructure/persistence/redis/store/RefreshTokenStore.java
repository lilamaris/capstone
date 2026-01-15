package com.lilamaris.capstone.refresh_token.infrastructure.persistence.redis.store;

import com.lilamaris.capstone.refresh_token.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.refresh_token.domain.RefreshToken;
import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RefreshTokenStore implements RefreshTokenPort {
    private final RedisTemplate<String, RefreshToken> template;

    @Override
    public RefreshToken consume(RefreshTokenId id) {
        var entry = consume(id.toString());
        if (entry == null) {
            throw new ResourceNotFoundException("Refresh token is invalid or expired.");
        }
        return entry;
    }

    @Override
    public void save(RefreshTokenId id, RefreshToken domain, Duration ttl) {
        save(id.asString(), domain, ttl);
    }

    public void save(String token, RefreshToken entry, Duration ttl) {
        template.opsForValue().set(key(token), entry, ttl);
    }

    private RefreshToken consume(String id) {
        return template.opsForValue().getAndDelete(key(id));
    }

    private String key(String token) {
        return "auth:refresh-token:" + token;
    }
}

