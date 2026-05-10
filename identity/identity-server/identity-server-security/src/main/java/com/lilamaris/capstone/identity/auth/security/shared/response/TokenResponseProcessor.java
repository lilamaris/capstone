package com.lilamaris.capstone.identity.auth.security.shared.response;

import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueJwtUseCase;
import com.lilamaris.capstone.identity.auth.application.jwks.port.in.IssueOpaqueTokenUseCase;
import com.lilamaris.capstone.identity.auth.security.shared.principal.TrustedPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenResponseProcessor {
    private final IssueJwtUseCase issueJwtUseCase;
    private final IssueOpaqueTokenUseCase issueOpaqueTokenUseCase;
    private final ResponseWriter responseWriter;

    public void process(@Nullable HttpServletResponse response, TrustedPrincipal principal) throws IOException {
        var subject = principal.userId().toString();
        var scopes = principal.scopes();

        var accessToken = issueJwtUseCase.issue(subject, scopes);
        var refreshToken = issueOpaqueTokenUseCase.issue();

        var tokenResponse = TokenResponse.of(accessToken, refreshToken);

        responseWriter.write(response, HttpStatus.OK, tokenResponse);
    }
}
