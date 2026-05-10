package com.lilamaris.capstone.identity.auth.application.jwks.port.out;

import com.lilamaris.capstone.identity.auth.domain.jwks.RSASignatureKey;
import com.lilamaris.capstone.identity.auth.domain.jwks.RSAVerificationKey;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public record KeyMaterial(String kid, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
    public RSASignatureKey toSignature() {
        if (privateKey == null)
            throw new IllegalStateException("signable key does not have a private key. kid=" + kid);
        return RSASignatureKey.of(kid, privateKey);
    }

    public RSAVerificationKey toVerification() {
        return RSAVerificationKey.of(kid, publicKey);
    }
}
