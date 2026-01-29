package com.lilamaris.capstone.auth.scenario.auth.application.port.in;

import com.lilamaris.capstone.auth.scenario.auth.application.result.AuthResult;

public interface CredentialAuthUseCase {
    AuthResult.Token signIn(String email, String passwordHash);

    AuthResult.Token register(String email, String passwordHash, String displayName);
}
