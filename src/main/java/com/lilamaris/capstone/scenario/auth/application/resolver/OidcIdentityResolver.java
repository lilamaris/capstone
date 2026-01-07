package com.lilamaris.capstone.scenario.auth.application.resolver;

import com.lilamaris.capstone.account.domain.Provider;

public interface OidcIdentityResolver {
    AuthIdentity resolve(Provider provider, String providerId, String email, String displayName);
}
