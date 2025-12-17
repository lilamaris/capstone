package com.lilamaris.capstone.adapter.in.security.authn.credential.token;

import com.lilamaris.capstone.application.port.in.result.AuthResult;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class CredentialSignInAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    private final AuthResult.Token token;
    private String rawPassword;

    public CredentialSignInAuthenticationToken(String email, String rawPassword) {
        super(null);
        setAuthenticated(false);
        this.email = email;
        this.rawPassword = rawPassword;
        this.token = null;
    }

    public CredentialSignInAuthenticationToken(AuthResult.Token tokenResult) {
        super(null);
        setAuthenticated(true);
        this.email = null;
        this.rawPassword = null;
        this.token = tokenResult;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.rawPassword = null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public Object getCredentials() {
        return rawPassword;
    }

    @Override
    public Object getDetails() {
        return token;
    }
}
