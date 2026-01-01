package com.lilamaris.capstone.shared.infrastructure.security.authn.credential.token;

import com.lilamaris.capstone.orchestration.auth.result.AuthResult;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class CredentialRegisterAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    private final String displayName;
    private final AuthResult.Token token;
    private String rawPassword;

    public CredentialRegisterAuthenticationToken(String email, String rawPassword, String displayName) {
        super(null);
        setAuthenticated(false);

        this.email = email;
        this.rawPassword = rawPassword;
        this.displayName = displayName;
        this.token = null;
    }

    public CredentialRegisterAuthenticationToken(AuthResult.Token tokenResult) {
        super(null);
        setAuthenticated(true);

        this.email = null;
        this.rawPassword = null;
        this.displayName = null;
        this.token = tokenResult;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.rawPassword = null;
    }

    @Override
    public Object getCredentials() {
        return rawPassword;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public Object getDetails() {
        return token;
    }
}
