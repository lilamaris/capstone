package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.auth.RefreshToken;

import java.time.Duration;

public interface RefreshTokenPort {
    RefreshToken consume(RefreshToken.Id id);
    void save(RefreshToken domain, Duration ttl);
}
