package com.lilamaris.capstone.adapter.out.redis.store;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.domain.auth.RefreshToken;
import com.lilamaris.capstone.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RefreshTokenStore implements RefreshTokenPort {
    private final RedisTemplate<String, RefreshTokenEntry> template;

    @Override
    public RefreshToken consume(RefreshToken.Id id) {
        var entry = consume(id.value());
        if (entry == null) {
            throw new ResourceNotFoundException("Refresh token is invalid or expired.");
        }
        return fromEntry(id, entry);
    }

    @Override
    public void save(RefreshToken domain, Duration ttl) {
        save(domain.id().value(), fromDomain(domain), ttl);
    }

    public void save(String token, RefreshTokenEntry entry, Duration ttl) {
        template.opsForValue().set(key(token), entry, ttl);
    }

    private RefreshTokenEntry consume(String token) {
        return template.opsForValue().getAndDelete(key(token));
    }

    private String key(String token) {
        return "auth:refresh-token:" + token;
    }

    private RefreshTokenEntry fromDomain(RefreshToken domain) {
        return new RefreshTokenEntry(domain.userId().asString());
    }

    private RefreshToken fromEntry(RefreshToken.Id id, RefreshTokenEntry entry) {
        return RefreshToken.builder().id(id).userId(User.Id.from(entry.userId())).build();
    }
}
