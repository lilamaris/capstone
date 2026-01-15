package com.lilamaris.capstone.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

import java.util.Optional;

public interface AuthUserResolver {
    Optional<UserEntry> resolve(ExternalizableId externalId);
}
