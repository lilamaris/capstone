package com.lilamaris.capstone.scenario.auth.application.service;

import com.lilamaris.capstone.scenario.auth.application.port.in.SessionIssuer;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthTokenRegistrar;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.shared.application.util.UniversityClock;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
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

    private final AuthTokenRegistrar authTokenRegistrar;

    private final Duration accessTokenExpiration;
    private final SecretKey secretKey;

    @Override
    public AuthResult.Token issue(ExternalizableId principalId, String displayName) {
        var accessToken = createAccessToken(principalId, displayName);
        var refreshToken = authTokenRegistrar.register(principalId);

        return AuthResult.Token.from(accessToken, refreshToken.token().asString());
    }

    private String createAccessToken(ExternalizableId principalId, String displayName) {
        var now = UniversityClock.now().toEpochMilli();
        var issuedAt = new Date(now);
        var expiredAt = new Date(now + accessTokenExpiration.toMillis());

        return Jwts.builder()
                .subject(principalId.asString())
                .claim(DISPLAY_NAME_KEY, displayName)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}
