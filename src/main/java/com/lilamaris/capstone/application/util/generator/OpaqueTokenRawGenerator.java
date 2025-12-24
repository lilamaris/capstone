package com.lilamaris.capstone.application.util.generator;

import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component("opaqueToken")
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
