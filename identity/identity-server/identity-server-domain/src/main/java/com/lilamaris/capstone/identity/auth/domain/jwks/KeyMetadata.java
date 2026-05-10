package com.lilamaris.capstone.identity.auth.domain.jwks;

public interface KeyMetadata {
    String kid();

    KeyType type();

    default boolean signable() {
        return type() == KeyType.SIGNABLE;
    }
}
