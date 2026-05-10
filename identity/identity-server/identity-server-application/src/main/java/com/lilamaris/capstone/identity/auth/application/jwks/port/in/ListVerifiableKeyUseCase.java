package com.lilamaris.capstone.identity.auth.application.jwks.port.in;

import com.lilamaris.capstone.identity.auth.domain.jwks.RSAVerificationKey;

import java.util.List;

public interface ListVerifiableKeyUseCase {
    List<RSAVerificationKey> list();
}
