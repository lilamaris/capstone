package com.lilamaris.capstone.adapter.in.security.authn.credential.provider;

import com.lilamaris.capstone.adapter.in.security.SecurityUserDetails;
import com.lilamaris.capstone.adapter.in.security.SecurityUserDetailsMapper;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialRegisterProvider implements AuthenticationProvider {
    private final AuthCommandUseCase authCommandUseCase;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var token = (RegisterAuthenticationToken) authentication;
        String email = (String) token.getPrincipal();
        String rawPassword = (String) token.getCredential();
        String displayName = token.getDisplayName();

        AuthResult.Register registerResult;
        try {
            registerResult = authCommandUseCase.credentialRegister(
                    email,
                    passwordEncoder.encode(rawPassword),
                    displayName
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Register failed.");
        }

        var userResult = registerResult.user();
        var accountResult = registerResult.account();
        var principal = SecurityUserDetailsMapper.from(userResult, accountResult);

        return new RegisterAuthenticationToken(principal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RegisterAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
