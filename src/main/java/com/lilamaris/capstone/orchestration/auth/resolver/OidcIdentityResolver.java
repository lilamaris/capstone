package com.lilamaris.capstone.orchestration.auth.resolver;

import com.lilamaris.capstone.account.domain.Provider;

public interface OidcIdentityResolver {
    AuthIdentity resolve(Provider provider, String providerId, String email, String displayName);
}
