package com.lilamaris.capstone.shared.infrastructure.security.authn.credential.provider;

import com.lilamaris.capstone.orchestration.auth.contract.TokenAuth;
import com.lilamaris.capstone.orchestration.auth.result.AuthResult;
import com.lilamaris.capstone.shared.application.exception.DomainViolationException;
import com.lilamaris.capstone.shared.infrastructure.security.authn.credential.token.RefreshTokenAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenProvider implements AuthenticationProvider {
    private final TokenAuth tokenAuth;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthResult.Token tokenResult;

        try {
            var token = (RefreshTokenAuthenticationToken) authentication;
            var refreshToken = token.getRefreshToken();

            tokenResult = tokenAuth.reissue(refreshToken);
        } catch (DomainViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("Refresh failed.");
        }

        return new RefreshTokenAuthenticationToken(tokenResult);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
