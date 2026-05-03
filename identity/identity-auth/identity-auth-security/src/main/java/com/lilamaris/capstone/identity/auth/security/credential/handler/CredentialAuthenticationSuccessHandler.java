package com.lilamaris.capstone.identity.auth.security.credential.handler;

import com.lilamaris.capstone.identity.auth.application.account.port.in.result.AuthenticationResult;
import com.lilamaris.capstone.identity.auth.security.shared.principal.SimpleTrustedPrincipal;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.NamespaceRole;
import com.lilamaris.capstone.identity.core.role.NamespaceRoleSerializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CredentialAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenResponseProcessor tokenResponseProcessor;
    private final NamespaceRoleSerializer namespaceRoleSerializer;

    @Override
    public void onAuthenticationSuccess(
            @Nullable HttpServletRequest request,
            @Nullable HttpServletResponse response,
            @Nullable Authentication authentication
    ) throws IOException, ServletException {
        if (authentication == null) return;
        if (!(authentication.getPrincipal() instanceof AuthenticationResult(
                UUID userId, String nickname, Set<NamespaceRole> grantedRoles
        ))) return;

        var scopes = grantedRoles.stream()
                .map(namespaceRoleSerializer::serialize)
                .collect(Collectors.toUnmodifiableSet());

        var trustedPrincipal = SimpleTrustedPrincipal.of(userId, nickname, scopes);

        tokenResponseProcessor.process(response, trustedPrincipal);
    }
}
