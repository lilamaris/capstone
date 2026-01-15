package com.lilamaris.capstone.scenario.auth.application.service;

import com.lilamaris.capstone.scenario.auth.application.exception.ExpiredTokenException;
import com.lilamaris.capstone.scenario.auth.application.exception.InvalidTokenException;
import com.lilamaris.capstone.scenario.auth.application.port.in.SessionIssuer;
import com.lilamaris.capstone.scenario.auth.application.port.in.TokenAuthUseCase;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthTokenResolver;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthUserResolver;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
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
    private final AuthTokenResolver authTokenResolver;
    private final AuthUserResolver userResolver;
    private final SessionIssuer sessionIssuer;
    private final SecretKey secretKey;

    @Override
    @Transactional
    public AuthResult.Token reissue(String refreshToken) {
        var tokenEntry = authTokenResolver.resolve(refreshToken);
        var userEntry = userResolver.resolve(tokenEntry.principal())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "User with id '%s' not found.", tokenEntry.principal()
                )));
        return sessionIssuer.issue(userEntry.id(), userEntry.displayName());
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
