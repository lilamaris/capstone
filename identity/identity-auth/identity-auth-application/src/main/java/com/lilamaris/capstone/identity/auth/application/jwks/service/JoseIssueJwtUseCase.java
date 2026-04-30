package com.lilamaris.capstone.identity.auth.application.jwks.service;

import com.lilamaris.capstone.identity.auth.application.jwks.contract.TokenIssuerMetadata;
import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueJwtUseCase;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Clock;
import java.util.Set;

public class JoseIssueJwtUseCase implements IssueJwtUseCase {
    private final JwtEncoder jwtEncoder;
    private final TokenIssuerMetadata metadata;
    private final Clock clock;

    public JoseIssueJwtUseCase(JwtEncoder jwtEncoder, TokenIssuerMetadata metadata, Clock clock) {
        this.jwtEncoder = Preconditions.requireNonNull(jwtEncoder, "jwtEncoder");
        this.metadata = Preconditions.requireNonNull(metadata, "metadata");
        this.clock = Preconditions.requireNonNull(clock, "clock");
    }

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
