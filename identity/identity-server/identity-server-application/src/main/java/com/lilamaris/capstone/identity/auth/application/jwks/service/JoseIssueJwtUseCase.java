package com.lilamaris.capstone.identity.auth.application.jwks.service;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueJwtUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.properties.IssueJwtProperties;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JoseIssueJwtUseCase implements IssueJwtUseCase {
    private final JwtEncoder jwtEncoder;
    private final IssueJwtProperties metadata;
    private final Clock clock;

    @Override
    public String issue(String subject, Set<String> scopes) {
        Preconditions.requireNonBlank(subject, "subject");
        Preconditions.requireNonNull(scopes, "scopes");

        var claims = buildClaims(subject, scopes);
        var parameters = JwtEncoderParameters.from(claims);
        return jwtEncoder.encode(parameters).getTokenValue();
    }

    private JwtClaimsSet buildClaims(String subject, Set<String> scopes) {
        var now = clock.instant();
        var expiresAt = now.plus(metadata.expiration());
        return JwtClaimsSet.builder()
                .issuer(metadata.issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(subject)
                .claim("scopes", scopes)
                .build();
    }
}
