package com.lilamaris.capstone.scenario.auth.application.port.in;

import com.lilamaris.capstone.account.domain.Provider;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;

public interface OidcAuthUseCase {
    AuthResult.Token signIn(Provider provider, String providerId, String email, String displayName);
}
