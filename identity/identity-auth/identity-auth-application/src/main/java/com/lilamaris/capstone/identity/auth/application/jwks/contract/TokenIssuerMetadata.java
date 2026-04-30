package com.lilamaris.capstone.identity.auth.application.jwks.contract;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.time.Duration;

public record TokenIssuerMetadata(
        String issuer,
        Duration expiration
) {
    public TokenIssuerMetadata {
        Preconditions.requireNonBlank(issuer, "issuer");
        Preconditions.requireNonNull(expiration, "expiration");
    }
}
