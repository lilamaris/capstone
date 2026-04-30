package com.lilamaris.capstone.identity.auth.application.jwks.service;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueOpaqueTokenUseCase;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.Base64;
import java.util.random.RandomGenerator;

public class RandomIssueOpaqueTokenUseCase implements IssueOpaqueTokenUseCase {
    private final RandomGenerator generator;
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public RandomIssueOpaqueTokenUseCase(RandomGenerator generator) {
        this.generator = Preconditions.requireNonNull(generator, "generator");
    }

    @Override
    public String issue() {
        byte[] randomBytes = new byte[32];
        generator.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }
}
