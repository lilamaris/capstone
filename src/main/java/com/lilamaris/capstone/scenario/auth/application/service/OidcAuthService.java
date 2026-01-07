package com.lilamaris.capstone.scenario.auth.application.service;

import com.lilamaris.capstone.account.domain.Provider;
import com.lilamaris.capstone.scenario.auth.application.port.in.OidcAuthUseCase;
import com.lilamaris.capstone.scenario.auth.application.resolver.OidcIdentityResolver;
import com.lilamaris.capstone.scenario.auth.application.resolver.SessionIssuer;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OidcAuthService implements OidcAuthUseCase {
    private final OidcIdentityResolver oidcIdentityResolver;
    private final SessionIssuer sessionIssuer;

    @Override
    @Transactional
    public AuthResult.Token signIn(Provider provider, String providerId, String email, String displayName) {
        var identity = oidcIdentityResolver.resolve(provider, providerId, email, displayName);
        return sessionIssuer.issue(identity);
    }
}
