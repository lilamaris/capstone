package com.lilamaris.capstone.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface AuthAccountRegistrar {
    AccountEntry register(
            ExternalizableId userId,
            String displayName,
            String email,
            String passwordHash
    );

    AccountEntry register(
            ExternalizableId userId,
            String provider,
            String providerId,
            String displayName,
            String email
    );
}
