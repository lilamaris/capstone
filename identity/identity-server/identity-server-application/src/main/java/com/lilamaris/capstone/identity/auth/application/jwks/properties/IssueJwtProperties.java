package com.lilamaris.capstone.identity.auth.application.jwks.properties;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "identity.jwt")
public record IssueJwtProperties(
        String issuer,
        Duration expiration
) {
    public IssueJwtProperties {
        Preconditions.requireNonBlank(issuer, "issuer");
        Preconditions.requireNonNull(expiration, "expiration");
    }
}
