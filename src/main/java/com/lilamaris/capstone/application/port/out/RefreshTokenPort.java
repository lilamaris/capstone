package com.lilamaris.capstone.application.port.out;


import com.lilamaris.capstone.domain.model.auth.refreshToken.RefreshToken;
import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;

import java.time.Duration;

public interface RefreshTokenPort {
    RefreshToken consume(RefreshTokenId id);

    void save(RefreshToken domain, Duration ttl);
}
