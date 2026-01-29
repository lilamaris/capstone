package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

import java.time.Duration;

public record AuthRefreshTokenRegisterEntry(
        ExternalizableId token,
        ExternalizableId principal,
        Duration ttl
) {
}
