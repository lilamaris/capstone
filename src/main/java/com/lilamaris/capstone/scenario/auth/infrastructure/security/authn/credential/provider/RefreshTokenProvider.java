package com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.provider;

import com.lilamaris.capstone.scenario.auth.application.port.in.TokenAuthUseCase;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.credential.token.RefreshTokenAuthenticationToken;
import com.lilamaris.capstone.shared.application.exception.DomainViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenProvider implements AuthenticationProvider {
    private final TokenAuthUseCase tokenAuthUseCase;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthResult.Token tokenResult;

        try {
            var token = (RefreshTokenAuthenticationToken) authentication;
            var refreshToken = token.getRefreshToken();

            tokenResult = tokenAuthUseCase.reissue(refreshToken);
        } catch (DomainViolationException e) {
            throw e;
        } catch (Exception e) {
            log.info("Refresh Error: {}", e.getMessage(), e);
            throw new AuthenticationServiceException("Refresh failed.");
        }

        return new RefreshTokenAuthenticationToken(tokenResult);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
