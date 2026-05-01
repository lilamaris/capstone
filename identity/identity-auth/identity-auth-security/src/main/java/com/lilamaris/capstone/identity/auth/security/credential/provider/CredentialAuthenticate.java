package com.lilamaris.capstone.identity.auth.security.credential.provider;

import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class CredentialAuthenticate extends AbstractAuthenticationToken {
    private final AuthenticationResult authenticationResult;

    private CredentialAuthenticate(
            AuthenticationResult authenticationResult
    ) {
        super(List.of());
        super.setAuthenticated(true);

        this.authenticationResult = authenticationResult;
    }

    public static CredentialAuthenticate of(AuthenticationResult authenticationResult) {
        return new CredentialAuthenticate(authenticationResult);
    }

    @Override
    public Object getPrincipal() {
        return authenticationResult;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
