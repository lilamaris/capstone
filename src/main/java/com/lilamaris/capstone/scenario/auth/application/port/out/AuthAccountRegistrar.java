package com.lilamaris.capstone.scenario.auth.application.port.out;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface AuthAccountRegistrar {
    AccountEntry register(
            ExternalizableId userId,
            String email,
            String passwordHash
    );

    AccountEntry register(
            ExternalizableId userId,
            AuthProvider authProvider,
            String principalId
    );
}
