package com.lilamaris.capstone.identity.auth.security.credential.provider;

import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateCredentialAccountUseCase;
import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationException;
import com.lilamaris.capstone.identity.auth.security.credential.filter.CredentialSignInAuthentication;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class CredentialSignInProvider implements AuthenticationProvider {
    private final AuthenticateCredentialAccountUseCase credentialAccountUseCase;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        var untrustedAuth = (CredentialSignInAuthentication) authentication;
        var command = untrustedAuth.toCommand();
        final AuthenticationResult authenticationResult;

        try {
            authenticationResult = credentialAccountUseCase.authenticate(command);
        } catch (IdentityAuthApplicationException e) {
            var errorCode = e.getErrorCode();
            if (errorCode == IdentityAuthApplicationErrorCode.AUTHENTICATION_FAILED) {
                throw new BadCredentialsException(e.getMessage());
            }
            throw new AuthenticationServiceException("Credential sign in failed.", e);
        }

        return CredentialAuthenticate.of(authenticationResult);
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return CredentialSignInAuthentication.class.isAssignableFrom(authentication);
    }
}
