package com.lilamaris.capstone.identity.auth.security.credential.provider;

import com.lilamaris.capstone.identity.auth.application.account.port.in.RegisterCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import com.lilamaris.capstone.identity.auth.security.credential.filter.CredentialSignUpAuthentication;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class CredentialSignUpProvider implements AuthenticationProvider {
    private final RegisterCredentialAccountUseCase credentialAccountUseCase;

    @Override
    public @Nullable Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        var untrustedAuth = (CredentialSignUpAuthentication) authentication;

        var command = untrustedAuth.toCommand();
        final AuthenticationResult authenticationResult;

        try {
            authenticationResult = credentialAccountUseCase.register(command);
        } catch (IdentityAuthApplicationException e) {
            throw switch (e.getErrorCode()) {
                case AUTHENTICATION_FAILED, CREDENTIAL_EMAIL_DUPLICATED ->
                        new AuthenticationServiceException(e.getMessage());
                default -> new AuthenticationServiceException("Credential sign up failed", e);
            };
        }

        return CredentialAuthenticate.of(authenticationResult);
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return CredentialSignUpAuthentication.class.isAssignableFrom(authentication);
    }
}
