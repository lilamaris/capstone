package com.lilamaris.capstone.scenario.auth.application.resolver;

import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;

public interface TokenIdentityResolver {
    AuthIdentity resolve(RefreshTokenId id);
}
