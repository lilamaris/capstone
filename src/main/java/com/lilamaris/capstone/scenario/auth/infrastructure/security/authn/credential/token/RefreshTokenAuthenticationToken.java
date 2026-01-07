package com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.token;

import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class RefreshTokenAuthenticationToken extends AbstractAuthenticationToken {
    private final AuthResult.Token token;
    private String refreshToken;

    public RefreshTokenAuthenticationToken(String refreshToken) {
        super(null);
        setAuthenticated(false);

        this.refreshToken = refreshToken;
        this.token = null;
    }

    public RefreshTokenAuthenticationToken(AuthResult.Token tokenResult) {
        super(null);
        setAuthenticated(true);

        this.refreshToken = null;
        this.token = tokenResult;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.refreshToken = null;
    }

    @Override
    public Object getCredentials() {
        return refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getDetails() {
        return token;
    }
}
