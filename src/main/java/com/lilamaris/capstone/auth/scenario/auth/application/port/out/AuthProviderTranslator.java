package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

public interface AuthProviderTranslator {
    AuthProviderIdentity translate(AuthProvider authProvider);
}
