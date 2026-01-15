package com.lilamaris.capstone.scenario.auth.application.port.out;

import org.springframework.lang.Nullable;

public record AuthVerifiedAccount(
        boolean success,
        @Nullable AuthAccountEntry entry
) {
}
