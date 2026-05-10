package com.lilamaris.capstone.identity.auth.domain.jwks;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.security.interfaces.RSAPrivateKey;

public class RSASignatureKey extends AbstractKeyMetadata {
    private final RSAPrivateKey privateKey;

    private RSASignatureKey(String kid, KeyType type, RSAPrivateKey privateKey) {
        super(kid, type);
        this.privateKey = Preconditions.requireNonNull(privateKey, "privateKey");
    }

    public static RSASignatureKey of(String kid, RSAPrivateKey privateKey) {
        return new RSASignatureKey(kid, KeyType.SIGNABLE, privateKey);
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }
}
