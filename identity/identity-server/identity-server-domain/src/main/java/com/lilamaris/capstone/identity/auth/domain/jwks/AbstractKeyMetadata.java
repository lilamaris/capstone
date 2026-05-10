package com.lilamaris.capstone.identity.auth.domain.jwks;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public class AbstractKeyMetadata implements KeyMetadata {
    private final String kid;
    private final KeyType type;

    protected AbstractKeyMetadata(String kid, KeyType type) {
        this.kid = Preconditions.requireNonBlank(kid, "kid");
        this.type = Preconditions.requireNonNull(type, "type");
    }

    @Override
    public String kid() {
        return kid;
    }

    @Override
    public KeyType type() {
        return type;
    }
}
