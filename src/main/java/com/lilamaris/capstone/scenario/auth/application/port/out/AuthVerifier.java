package com.lilamaris.capstone.scenario.auth.application.port.out;

public interface AuthVerifier {
    AuthVerifiedAccount verify(AuthProvider authProvider, String principalId, String challenge);
}
