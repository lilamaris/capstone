package com.lilamaris.capstone.identity.auth.security.federated.handler;

import com.lilamaris.capstone.identity.auth.application.account.port.in.AuthenticateFederatedAccountUseCase;
import com.lilamaris.capstone.identity.auth.security.federated.principal.FederatedPrincipal;
import com.lilamaris.capstone.identity.auth.security.shared.principal.SimpleTrustedPrincipal;
import com.lilamaris.capstone.identity.auth.security.shared.response.TokenResponseProcessor;
import com.lilamaris.capstone.identity.core.role.NamespaceRoleSerializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FederatedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AuthenticateFederatedAccountUseCase accountUseCase;
    private final NamespaceRoleSerializer namespaceRoleSerializer;
    private final TokenResponseProcessor tokenResponseProcessor;

    @Override
    public void onAuthenticationSuccess(
            @Nullable HttpServletRequest request,
            @Nullable HttpServletResponse response,
            @Nullable Authentication authentication
    ) throws IOException, ServletException {
        if (authentication == null) return;
        if (!(authentication.getPrincipal() instanceof FederatedPrincipal principal)) return;

        var command = principal.toCommand();
        var auth = accountUseCase.authenticate(command);

        var scopes = auth.grantedRoles().stream()
                .map(namespaceRoleSerializer::serialize)
                .collect(Collectors.toUnmodifiableSet());

        var trustedPrincipal = SimpleTrustedPrincipal.of(auth.userId(), auth.nickname(), scopes);
        tokenResponseProcessor.process(response, trustedPrincipal);
    }
}
