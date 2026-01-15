package com.lilamaris.capstone.scenario.auth.application.port.out;

import org.springframework.lang.Nullable;

public record VerifiedAccount(
        boolean success,
        @Nullable AccountEntry entry
) {
}
