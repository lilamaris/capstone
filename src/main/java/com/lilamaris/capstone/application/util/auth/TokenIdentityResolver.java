package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.domain.auth.RefreshToken;

public interface TokenIdentityResolver {
    AuthIdentity resolve(RefreshToken.Id id);
}
