package com.lilamaris.capstone.identity.auth.application.jwks.service;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueOpaqueTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class RandomIssueOpaqueTokenUseCase implements IssueOpaqueTokenUseCase {
    private final RandomGenerator generator;
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    @Override
    public String issue() {
        byte[] randomBytes = new byte[32];
        generator.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }
}
