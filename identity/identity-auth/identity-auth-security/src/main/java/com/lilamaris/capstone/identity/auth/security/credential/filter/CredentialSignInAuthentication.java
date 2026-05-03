package com.lilamaris.capstone.identity.auth.security.credential.filter;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.AuthenticateCredentialAccountCommand;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class CredentialSignInAuthentication extends AbstractAuthenticationToken {
    private final String email;
    private String password;

    private CredentialSignInAuthentication(String email, String password) {
        super(List.of());
        setAuthenticated(false);

        this.email = Preconditions.requireNonBlank(email, "email");
        this.password = Preconditions.requireNonBlank(password, "password");
    }

    public static CredentialSignInAuthentication of(String email, String password) {
        return new CredentialSignInAuthentication(email, password);
    }

    public AuthenticateCredentialAccountCommand toCommand() {
        return new AuthenticateCredentialAccountCommand(email, password);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.password = null;
    }

    @Override
    public @Nullable Object getCredentials() {
        return password;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return email;
    }
}
