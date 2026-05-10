package com.lilamaris.capstone.identity.auth.persistence.jwks.io;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "identity.jwks")
public record JwksFileProperties(
        String activeSignableKid,
        String keyBaseLocation,
        Set<String> keys
) {
}
