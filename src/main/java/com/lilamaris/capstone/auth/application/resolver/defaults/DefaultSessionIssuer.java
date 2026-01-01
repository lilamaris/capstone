package com.lilamaris.capstone.auth.application.resolver.defaults;

import com.lilamaris.capstone.auth.application.resolver.AuthIdentity;
import com.lilamaris.capstone.auth.application.resolver.SessionIssuer;
import com.lilamaris.capstone.auth.application.result.AuthResult;
import com.lilamaris.capstone.auth.application.util.JwtUtil;
import com.lilamaris.capstone.refresh_token.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.refresh_token.domain.RefreshToken;
import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.shared.application.identity.IdGenerationContext;
import com.lilamaris.capstone.user.domain.Role;
import com.lilamaris.capstone.user.domain.id.UserId;
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

        var refreshToken = RefreshToken.create(userId, ids.next(RefreshTokenId.class));

        refreshTokenPort.save(refreshToken, jwtUtil.getRefreshTokenExpiration());

        return AuthResult.Token.from(accessTokenValue, refreshToken.id().toString());
    }
}
