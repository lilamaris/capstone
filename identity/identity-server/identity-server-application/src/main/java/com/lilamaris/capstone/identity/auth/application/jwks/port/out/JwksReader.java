package com.lilamaris.capstone.identity.auth.application.jwks.port.out;

import com.lilamaris.capstone.identity.auth.domain.jwks.RSAVerificationKey;

import java.util.List;

public interface JwksReader {
    KeyMaterial findSignableKey();

    List<RSAVerificationKey> findVerifiableKeys();
}
