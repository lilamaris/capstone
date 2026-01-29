package com.lilamaris.capstone.auth.scenario.auth.application.port.in;

import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthProvider;
import com.lilamaris.capstone.auth.scenario.auth.application.result.AuthResult;

public interface OidcAuthUseCase {
    AuthResult.Token signIn(AuthProvider authProvider, String providerId, String email, String displayName);
}
