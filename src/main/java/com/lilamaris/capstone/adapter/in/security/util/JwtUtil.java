package com.lilamaris.capstone.adapter.in.security.util;

import com.lilamaris.capstone.adapter.in.security.SecurityUserDetails;
import com.lilamaris.capstone.adapter.in.security.exception.ExpiredTokenException;
import com.lilamaris.capstone.adapter.in.security.exception.InvalidTokenException;
import com.lilamaris.capstone.application.util.UniversityClock;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    public static final String DISPLAY_NAME_KEY = "display";
    public static final String AUTHORITIES_KEY = "role";

    @Value("${spring.security.jwt.accessTokenExpiration}")
    private Duration accessTokenExpiration;

    @Value("${spring.security.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    private final SecretKey key;

    public JwtUtil(@Value("${spring.security.jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createAccessToken(SecurityUserDetails principal) {
        var now = UniversityClock.now().toEpochMilli();
        var issuedAt = new Date(now);
        var expiredAt = new Date(now + accessTokenExpiration.toMillis());

        var userId = principal.getUserId();
        var displayName = principal.getDisplayName();
        var roleName = principal.getRole().name();

        return Jwts.builder()
                .subject(userId)
                .claim(DISPLAY_NAME_KEY, displayName)
                .claim(AUTHORITIES_KEY, roleName)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(SecurityUserDetails principal) {
        var now = UniversityClock.now().toEpochMilli();
        var issuedAt = new Date(now);
        var expiredAt = new Date(now + refreshTokenExpiration.toMillis());

        var userId = principal.getUserId();

        return Jwts.builder()
                .subject(userId)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
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
        } catch (SecurityException | MalformedJwtException e) {
            log.debug("Wrong jwt signature: ", e);
            throw new InvalidTokenException("Invalid token.");
        } catch (ExpiredJwtException e) {
            log.debug("Expired jwt", e);
            throw new ExpiredTokenException("Expired token.");
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported jwt", e);
            throw new InvalidTokenException("Invalid token");
        } catch (IllegalArgumentException e) {
            log.debug("Invalid jwt", e);
            throw new InvalidTokenException("Invalid token");
        }
    }
}