package com.lilamaris.capstone.orchestration.auth.contract;

import com.lilamaris.capstone.account.domain.Provider;
import com.lilamaris.capstone.orchestration.auth.result.AuthResult;

public interface OidcAuth {
    AuthResult.Token signIn(Provider provider, String providerId, String email, String displayName);
}
