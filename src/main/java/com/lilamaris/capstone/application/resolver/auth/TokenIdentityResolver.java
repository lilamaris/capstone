package com.lilamaris.capstone.application.resolver.auth;

import com.lilamaris.capstone.domain.auth.RefreshToken;

public interface TokenIdentityResolver {
    AuthIdentity resolve(RefreshToken.Id id);
}
