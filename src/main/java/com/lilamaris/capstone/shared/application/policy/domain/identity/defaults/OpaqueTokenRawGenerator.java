package com.lilamaris.capstone.shared.application.policy.domain.identity.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;

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
