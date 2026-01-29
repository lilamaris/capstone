package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

import java.util.Optional;

public interface AuthUserResolver {
    Optional<AuthUserEntry> resolve(ExternalizableId externalId);
}
