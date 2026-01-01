package com.lilamaris.capstone.auth.application.result;

import lombok.Builder;

public class AuthResult {
    @Builder
    public record Token(
            String accessToken,
            String refreshToken
    ) {
        public static Token from(String accessToken, String refreshToken) {
            return builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }
}
