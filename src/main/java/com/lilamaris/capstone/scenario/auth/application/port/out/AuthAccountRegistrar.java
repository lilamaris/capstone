package com.lilamaris.capstone.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface AuthAccountRegistrar {
    AuthAccountEntry register(
            ExternalizableId userId,
            String email,
            String passwordHash
    );

    AuthAccountEntry register(
            ExternalizableId userId,
            AuthProvider authProvider,
            String principalId
    );
}
