package com.lilamaris.capstone.adapter.in.security.authn.oidc.handler;

import com.lilamaris.capstone.adapter.in.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.adapter.in.security.util.ResponseWriter;
import com.lilamaris.capstone.application.exception.DomainViolationException;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOidcSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthCommandUseCase authCommandUseCase;

    private final ResponseWriter writer;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        AuthResult.Token tokenResult;

        try {
            NormalizedProfile principal = (NormalizedProfile) authentication.getPrincipal();
            var provider = principal.getProvider();
            var providerId = principal.getProviderId();
            var email = principal.getEmail();
            var displayName = principal.getDisplayName();

            tokenResult = authCommandUseCase.oidcSignIn(provider, providerId, email, displayName);
        } catch (DomainViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("SignIn failed.", e);
        }

        writer.token(response, tokenResult);
    }
}
