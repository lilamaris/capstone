package com.lilamaris.capstone.refresh_token.application.port.out;


import com.lilamaris.capstone.refresh_token.domain.RefreshToken;
import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;

import java.time.Duration;

public interface RefreshTokenPort {
    RefreshToken consume(RefreshTokenId id);

    void save(RefreshTokenId id, RefreshToken domain, Duration ttl);
}
