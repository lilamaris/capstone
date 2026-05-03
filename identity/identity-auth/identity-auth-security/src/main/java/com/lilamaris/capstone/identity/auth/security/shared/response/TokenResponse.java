package com.lilamaris.capstone.identity.auth.security.shared.response;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
    public TokenResponse {
        Preconditions.requireNonBlank(accessToken, "accessToken");
        Preconditions.requireNonBlank(refreshToken, "refreshToken");
    }

    public static TokenResponse of(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }
}
