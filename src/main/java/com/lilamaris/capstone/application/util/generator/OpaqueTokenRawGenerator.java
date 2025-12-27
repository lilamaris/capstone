package com.lilamaris.capstone.application.util.generator;

import com.lilamaris.capstone.domain.model.common.domain.id.RawGenerator;

import java.security.SecureRandom;
import java.util.Base64;

public class OpaqueTokenRawGenerator implements RawGenerator<String> {
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    @Override
    public String generate() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }
}
