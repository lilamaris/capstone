package com.lilamaris.capstone.scenario.auth.application.port.out;

public interface ProviderTranslator {
    AuthProviderIdentity translate(AuthProvider authProvider);
}
