package com.lilamaris.capstone.identity.auth.security.shared.principal;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.Set;
import java.util.UUID;

public record SimpleTrustedPrincipal(
        UUID userId,
        String nickname,
        Set<String> scopes
) implements TrustedPrincipal {
    public SimpleTrustedPrincipal {
        Preconditions.requireNonNull(userId, "userId");
        Preconditions.requireNonBlank(nickname, "nickname");
        Preconditions.requireNonNull(scopes, "scopes");
    }

    public static SimpleTrustedPrincipal of(UUID userId, String nickname, Set<String> scopes) {
        return new SimpleTrustedPrincipal(userId, nickname, scopes);
    }
}
