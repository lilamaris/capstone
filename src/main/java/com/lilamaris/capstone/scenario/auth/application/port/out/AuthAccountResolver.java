package com.lilamaris.capstone.scenario.auth.application.port.out;

import java.util.Optional;

public interface AuthAccountResolver {
    Optional<AccountEntry> resolve(AuthProvider provider, String principalId);
}
