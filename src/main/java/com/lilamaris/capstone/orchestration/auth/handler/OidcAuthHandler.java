package com.lilamaris.capstone.orchestration.auth.handler;

import com.lilamaris.capstone.account.domain.Provider;
import com.lilamaris.capstone.orchestration.auth.contract.OidcAuth;
import com.lilamaris.capstone.orchestration.auth.resolver.OidcIdentityResolver;
import com.lilamaris.capstone.orchestration.auth.resolver.SessionIssuer;
import com.lilamaris.capstone.orchestration.auth.result.AuthResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OidcAuthHandler implements OidcAuth {
    private final OidcIdentityResolver oidcIdentityResolver;
    private final SessionIssuer sessionIssuer;

    @Override
    @Transactional
    public AuthResult.Token signIn(Provider provider, String providerId, String email, String displayName) {
        var identity = oidcIdentityResolver.resolve(provider, providerId, email, displayName);
        return sessionIssuer.issue(identity);
    }
}
