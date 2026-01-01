package com.lilamaris.capstone.shared.infrastructure.security.authn.oidc.handler;

import com.lilamaris.capstone.orchestration.auth.contract.OidcAuth;
import com.lilamaris.capstone.orchestration.auth.result.AuthResult;
import com.lilamaris.capstone.shared.application.exception.DomainViolationException;
import com.lilamaris.capstone.shared.infrastructure.security.authn.oidc.NormalizedProfile;
import com.lilamaris.capstone.shared.infrastructure.security.util.ResponseWriter;
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
    private final OidcAuth oidcAuth;

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

            tokenResult = oidcAuth.signIn(provider, providerId, email, displayName);
        } catch (DomainViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException("SignIn failed.", e);
        }

        writer.token(response, tokenResult);
    }
}
