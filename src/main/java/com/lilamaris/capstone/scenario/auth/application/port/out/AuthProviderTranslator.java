package com.lilamaris.capstone.scenario.auth.application.port.out;

public interface AuthProviderTranslator {
    AuthProviderIdentity translate(AuthProvider authProvider);
}
