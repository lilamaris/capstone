package com.lilamaris.capstone.identity.auth.security.credential.handler;

import com.lilamaris.capstone.identity.auth.security.credential.provider.CredentialTrustedAuthenticate;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CredentialAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenResponseProcessor tokenResponseProcessor;

    @Override
    public void onAuthenticationSuccess(
            @Nullable HttpServletRequest request,
            @Nullable HttpServletResponse response,
            @Nullable Authentication authentication
    ) throws IOException, ServletException {
        if (authentication == null) return;
        if (!(authentication instanceof CredentialTrustedAuthenticate auth)) return;

        tokenResponseProcessor.process(response, auth);
    }
}
