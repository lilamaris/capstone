package com.lilamaris.capstone.adapter.out.redis.store;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.domain.model.auth.refreshToken.RefreshToken;
import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;
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
    public void save(RefreshToken domain, Duration ttl) {
        save(domain.id().toString(), domain, ttl);
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
