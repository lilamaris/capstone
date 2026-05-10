package com.lilamaris.capstone.identity.auth.security.shared.response;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 성공 후 발급된 토큰 응답")
public record TokenResponse(
        @Schema(description = "API 인증에 사용할 액세스 토큰")
        String accessToken,
        @Schema(description = "액세스 토큰 재발급에 사용할 리프레시 토큰")
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
