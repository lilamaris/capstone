package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.application.port.in.result.AuthResult;
import com.lilamaris.capstone.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.domain.model.auth.refreshToken.RefreshTokenFactory;
import com.lilamaris.capstone.domain.model.capstone.user.Role;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultSessionIssuer implements SessionIssuer {
    private final JwtUtil jwtUtil;
    private final RefreshTokenPort refreshTokenPort;

    private final RefreshTokenFactory refreshTokenFactory;

    @Override
    public AuthResult.Token issue(AuthIdentity identity) {
        UserId userId = identity.user().id();
        String displayName = identity.user().getDisplayName();
        Role role = identity.user().getRole();

        String accessTokenValue = jwtUtil.createAccessToken(userId, displayName, role);
        String refreshTokenValue = jwtUtil.createRefreshToken();

        var refreshToken = refreshTokenFactory.create(userId);

        refreshTokenPort.save(refreshToken, jwtUtil.getRefreshTokenExpiration());

        return AuthResult.Token.from(accessTokenValue, refreshTokenValue);
    }
}
