package com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.provider;

import com.lilamaris.capstone.scenario.auth.application.port.in.CredentialAuthUseCase;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.token.CredentialRegisterAuthenticationToken;
import com.lilamaris.capstone.shared.application.exception.DomainViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialRegisterProvider implements AuthenticationProvider {
    private final CredentialAuthUseCase credentialRegister;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthResult.Token tokenResult;

        try {
            var token = (CredentialRegisterAuthenticationToken) authentication;
            var email = token.getEmail();
            var rawPassword = token.getRawPassword();
            var displayName = token.getDisplayName();
            var passwordHash = passwordEncoder.encode(rawPassword);

            tokenResult = credentialRegister.register(email, passwordHash, displayName);
        } catch (DomainViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("Register failed.", e);
        }

        return new CredentialRegisterAuthenticationToken(tokenResult);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CredentialRegisterAuthenticationToken.class.isAssignableFrom(authentication);
    }
}