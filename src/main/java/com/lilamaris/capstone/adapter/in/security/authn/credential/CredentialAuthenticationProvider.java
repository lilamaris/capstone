package com.lilamaris.capstone.adapter.in.security.authn.credential;

import com.lilamaris.capstone.adapter.in.security.SecurityUserDetailsMapper;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialAuthenticationProvider implements AuthenticationProvider {
    private final AuthCommandUseCase authCommandUseCase;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        AuthResult.Login loginResult;
        try {
            loginResult = authCommandUseCase.credentialLogin(
                    email,
                    (hash) -> passwordEncoder.matches(password, hash)
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Login failed.");
        }

        var userResult = loginResult.user();
        var accountResult = loginResult.account();
        var principal = SecurityUserDetailsMapper.from(userResult, accountResult);

        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
