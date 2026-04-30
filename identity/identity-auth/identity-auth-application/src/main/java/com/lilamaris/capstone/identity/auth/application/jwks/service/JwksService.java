package com.lilamaris.capstone.identity.auth.application.jwks.service;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.ListVerifiableKeyUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.port.out.JwksReader;
import com.lilamaris.capstone.identity.auth.domain.jwks.RSAVerificationKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwksService implements ListVerifiableKeyUseCase {
    private final JwksReader reader;

    @Override
    public List<RSAVerificationKey> list() {
        return reader.findVerifiableKeys();
    }
}
