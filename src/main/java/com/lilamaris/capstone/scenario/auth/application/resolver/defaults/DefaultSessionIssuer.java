package com.lilamaris.capstone.scenario.auth.application.resolver.defaults;

import com.lilamaris.capstone.refresh_token.application.port.out.RefreshTokenPort;
import com.lilamaris.capstone.refresh_token.domain.RefreshToken;
import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.scenario.auth.application.resolver.AuthIdentity;
import com.lilamaris.capstone.scenario.auth.application.resolver.SessionIssuer;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.application.util.UniversityClock;
import com.lilamaris.capstone.user.domain.Role;
import com.lilamaris.capstone.user.domain.id.UserId;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DefaultSessionIssuer implements SessionIssuer {
    public static final String DISPLAY_NAME_KEY = "display";
    public static final String AUTHORITIES_KEY = "role";

    private final RefreshTokenPort refreshTokenPort;
    private final IdGenerationDirectory ids;

    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;
    private final SecretKey secretKey;

    @Override
    public AuthResult.Token issue(AuthIdentity identity) {
        UserId userId = identity.user().id();
        String displayName = identity.user().getDisplayName();
        Role role = identity.user().getRole();

        String accessTokenValue = createAccessToken(userId, displayName, role);

        var refreshToken = RefreshToken.create(userId, ids.next(RefreshTokenId.class));

        refreshTokenPort.save(refreshToken, refreshTokenExpiration);

        return AuthResult.Token.from(accessTokenValue, refreshToken.id().toString());
    }

    private String createAccessToken(UserId userId, String displayName, Role role) {
        var now = UniversityClock.now().toEpochMilli();
        var issuedAt = new Date(now);
        var expiredAt = new Date(now + accessTokenExpiration.toMillis());

        return Jwts.builder()
                .subject(userId.toString())
                .claim(DISPLAY_NAME_KEY, displayName)
                .claim(AUTHORITIES_KEY, role.name())
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}
