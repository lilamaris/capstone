package com.lilamaris.capstone.scenario.auth.application.service;

import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.scenario.auth.application.exception.ExpiredTokenException;
import com.lilamaris.capstone.scenario.auth.application.exception.InvalidTokenException;
import com.lilamaris.capstone.scenario.auth.application.port.in.TokenAuthUseCase;
import com.lilamaris.capstone.scenario.auth.application.resolver.SessionIssuer;
import com.lilamaris.capstone.scenario.auth.application.resolver.TokenIdentityResolver;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class TokenAuthService implements TokenAuthUseCase {
    private final TokenIdentityResolver tokenIdentityResolver;
    private final SessionIssuer sessionIssuer;
    private final SecretKey secretKey;

    @Override
    @Transactional
    public AuthResult.Token reissue(String refreshToken) {
        var identity = tokenIdentityResolver.resolve(new RefreshTokenId(refreshToken));
        return sessionIssuer.issue(identity);
    }

    @Override
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("Expired token.");
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token.");
        }
    }
}
