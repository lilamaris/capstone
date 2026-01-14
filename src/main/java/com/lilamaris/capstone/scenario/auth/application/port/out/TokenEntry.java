package com.lilamaris.capstone.scenario.auth.application.port.out;

import java.time.Instant;

public record TokenEntry(
        String token,
        Instant ttl
) {
}
