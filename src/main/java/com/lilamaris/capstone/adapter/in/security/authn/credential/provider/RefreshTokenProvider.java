package com.lilamaris.capstone.adapter.in.security.authn.credential.provider;

import com.lilamaris.capstone.adapter.in.security.authn.credential.token.RefreshTokenAuthenticationToken;
import com.lilamaris.capstone.application.exception.DomainViolationException;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenProvider implements AuthenticationProvider {
    private final AuthCommandUseCase authCommandUseCase;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthResult.Token tokenResult;

        try {
            var token = (RefreshTokenAuthenticationToken) authentication;
            var refreshToken = token.getRefreshToken();

            tokenResult = authCommandUseCase.refresh(refreshToken);
        } catch (DomainViolationException e) {
            throw e;
        } catch(Exception e) {
            throw new AuthenticationServiceException("Refresh failed.");
        }

        return new RefreshTokenAuthenticationToken(tokenResult);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
