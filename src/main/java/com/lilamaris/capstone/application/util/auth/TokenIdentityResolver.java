package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;

public interface TokenIdentityResolver {
    AuthIdentity resolve(RefreshTokenId id);
}
