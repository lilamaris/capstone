package com.lilamaris.capstone.application.resolver.auth;

import com.lilamaris.capstone.application.port.in.result.AuthResult;
import com.lilamaris.capstone.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.application.util.JwtUtil;
import com.lilamaris.capstone.domain.auth.RefreshToken;
import com.lilamaris.capstone.domain.user.Role;
import com.lilamaris.capstone.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultSessionIssuer implements SessionIssuer{
    private final JwtUtil jwtUtil;
    private final RefreshTokenPort refreshTokenPort;

    @Override
    public AuthResult.Token issue(AuthIdentity identity) {
        User.Id userId= identity.user().id();
        String displayName = identity.user().displayName();
        Role role = identity.user().role();

        String accessTokenValue = jwtUtil.createAccessToken(userId, displayName, role);
        String refreshTokenValue = jwtUtil.createRefreshToken();

        var refreshToken = RefreshToken.builder()
                .id(RefreshToken.Id.from(refreshTokenValue))
                .userId(userId)
                .build();

        refreshTokenPort.save(refreshToken, jwtUtil.getRefreshTokenExpiration());

        return AuthResult.Token.from(accessTokenValue, refreshTokenValue);
    }
}
