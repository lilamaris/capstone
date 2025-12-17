package com.lilamaris.capstone.adapter.in.security.authn.credential.provider;

import com.lilamaris.capstone.adapter.in.security.authn.credential.token.CredentialSignInAuthenticationToken;
import com.lilamaris.capstone.application.exception.DomainViolationException;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CredentialSignInProvider implements AuthenticationProvider {
    private final AuthCommandUseCase authCommandUseCase;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthResult.Token tokenResult;

        try {
            var token = (CredentialSignInAuthenticationToken) authentication;
            var email = token.getEmail();
            var rawPassword = token.getRawPassword();
            Function<String, Boolean> challenge = (hash) -> passwordEncoder.matches(rawPassword, hash);

            tokenResult = authCommandUseCase.credentialSignIn(email, challenge);
        } catch (DomainViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("SignIn failed.");
        }

        return new CredentialSignInAuthenticationToken(tokenResult);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CredentialSignInAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
