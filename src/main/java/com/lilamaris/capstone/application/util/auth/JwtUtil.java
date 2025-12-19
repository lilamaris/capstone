package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.application.exception.ApplicationInvariantException;
import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.user.Role;
import com.lilamaris.capstone.domain.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    public static final String DISPLAY_NAME_KEY = "display";
    public static final String AUTHORITIES_KEY = "role";

    @Getter
    private final Duration accessTokenExpiration;

    @Getter
    private final Duration refreshTokenExpiration;

    private final SecureRandom secureRandom;
    private final Base64.Encoder encoder;
    private final SecretKey key;

    public JwtUtil(
            @Value("${spring.security.jwt.accessTokenExpiration}") Duration accessTokenExpiration,
            @Value("${spring.security.jwt.refreshTokenExpiration}") Duration refreshTokenExpiration,
            @Value("${spring.security.jwt.secret}") String secret
    ) {
        this.secureRandom = new SecureRandom();
        this.encoder = Base64.getUrlEncoder().withoutPadding();
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.accessTokenExpiration = accessTokenExpiration;

        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createAccessToken(User.Id userId, String displayName, Role role) {
        var now = UniversityClock.now().toEpochMilli();
        var issuedAt = new Date(now);
        var expiredAt = new Date(now + accessTokenExpiration.toMillis());

        return Jwts.builder()
                .subject(userId.getValue().toString())
                .claim(DISPLAY_NAME_KEY, displayName)
                .claim(AUTHORITIES_KEY, role.name())
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken() {
        return genOpaqueString();
    }

    public Claims parseToken(String token) throws SecurityException {
        try {
            var claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            if (claims.get(AUTHORITIES_KEY) == null) {
                throw new IllegalArgumentException("Empty authorities field.");
            }
            return claims;
        } catch (ExpiredJwtException e) {
            throw new ApplicationInvariantException("EXPIRED_TOKEN", "Expired token.");
        } catch (Exception e) {
            throw new ApplicationInvariantException("INVALID_TOKEN", "Invalid token.");
        }
    }

    public String genOpaqueString() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }
}