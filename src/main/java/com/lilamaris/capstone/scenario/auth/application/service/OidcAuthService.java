package com.lilamaris.capstone.scenario.auth.application.service;

import com.lilamaris.capstone.scenario.auth.application.port.in.OidcAuthUseCase;
import com.lilamaris.capstone.scenario.auth.application.port.in.SessionIssuer;
import com.lilamaris.capstone.scenario.auth.application.port.out.*;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OidcAuthService implements OidcAuthUseCase {
    private final AuthAccountResolver accountResolver;
    private final AuthAccountRegistrar accountRegistrar;
    private final AuthUserResolver userResolver;
    private final AuthUserRegistrar userRegistrar;
    private final SessionIssuer sessionIssuer;

    @Override
    @Transactional
    public AuthResult.Token signIn(AuthProvider authProvider, String providerId, String email, String displayName) {
        var userId = accountResolver.resolve(authProvider, providerId)
                .map(AuthAccountEntry::userId);
        var userEntry = userId
                .map(id ->
                        userResolver.resolve(id)
                                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                                        "User with id '%s' not found", id
                                )))
                )
                .orElseGet(() -> {
                    var user = userRegistrar.register(displayName);
                    accountRegistrar.register(user.id(), authProvider, providerId);
                    return user;
                });

        return sessionIssuer.issue(userEntry.id(), userEntry.displayName());
    }
}
