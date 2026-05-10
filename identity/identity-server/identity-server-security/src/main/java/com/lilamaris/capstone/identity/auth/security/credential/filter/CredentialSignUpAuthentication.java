package com.lilamaris.capstone.identity.auth.security.credential.filter;

import com.lilamaris.capstone.identity.auth.application.account.port.in.command.RegisterCredentialAccountCommand;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class CredentialSignUpAuthentication extends AbstractAuthenticationToken {
    private final String nickname;
    private final String email;
    private String password;

    private CredentialSignUpAuthentication(String nickname, String email, String password) {
        super(List.of());
        setAuthenticated(false);

        this.nickname = Preconditions.requireNonBlank(nickname, "nickname");
        this.email = Preconditions.requireNonBlank(email, "email");
        this.password = Preconditions.requireNonBlank(password, "password");
    }

    public static CredentialSignUpAuthentication of(String nickname, String email, String password) {
        return new CredentialSignUpAuthentication(nickname, email, password);
    }

    public RegisterCredentialAccountCommand toCommand() {
        return new RegisterCredentialAccountCommand(nickname, email, password);
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
