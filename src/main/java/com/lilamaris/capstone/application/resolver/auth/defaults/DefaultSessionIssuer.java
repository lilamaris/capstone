package com.lilamaris.capstone.application.resolver.auth.defaults;

import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import com.lilamaris.capstone.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.application.resolver.auth.AuthIdentity;
import com.lilamaris.capstone.application.resolver.auth.SessionIssuer;
import com.lilamaris.capstone.application.util.auth.JwtUtil;
import com.lilamaris.capstone.domain.model.auth.refreshToken.RefreshToken;
import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;
import com.lilamaris.capstone.domain.model.capstone.user.Role;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultSessionIssuer implements SessionIssuer {
    private final JwtUtil jwtUtil;
    private final RefreshTokenPort refreshTokenPort;

    private final IdGenerationContext ids;

    @Override
    public AuthResult.Token issue(AuthIdentity identity) {
        UserId userId = identity.user().id();
        String displayName = identity.user().getDisplayName();
        Role role = identity.user().getRole();

        String accessTokenValue = jwtUtil.createAccessToken(userId, displayName, role);
        String refreshTokenValue = jwtUtil.createRefreshToken();

        var refreshToken = RefreshToken.create(userId, () -> ids.next(RefreshTokenId.class));

        refreshTokenPort.save(refreshToken, jwtUtil.getRefreshTokenExpiration());

        return AuthResult.Token.from(accessTokenValue, refreshTokenValue);
    }
}
