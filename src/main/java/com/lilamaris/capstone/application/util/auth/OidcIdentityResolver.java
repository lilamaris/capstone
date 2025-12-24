package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.domain.model.auth.account.Provider;

public interface OidcIdentityResolver {
    AuthIdentity resolve(Provider provider, String providerId, String email, String displayName);
}
