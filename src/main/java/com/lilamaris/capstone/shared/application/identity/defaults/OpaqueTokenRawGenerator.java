package com.lilamaris.capstone.shared.application.identity.defaults;

import com.lilamaris.capstone.shared.application.identity.contract.RawGenerator;

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
