package com.lilamaris.capstone.identity.auth.domain.jwks;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.security.interfaces.RSAPublicKey;

public class RSAVerificationKey extends AbstractKeyMetadata {
    private final RSAPublicKey publicKey;

    private RSAVerificationKey(String kid, KeyType type, RSAPublicKey publicKey) {
        super(kid, type);
        this.publicKey = Preconditions.requireNonNull(publicKey, "publicKey");
    }

    public static RSAVerificationKey of(String kid, RSAPublicKey publicKey) {
        return new RSAVerificationKey(kid, KeyType.VERIFIABLE, publicKey);
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }
}
