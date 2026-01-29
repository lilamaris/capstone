package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

public interface AuthVerifier {
    AuthVerifiedAccount verify(AuthProvider authProvider, String principalId, String challenge);
}
