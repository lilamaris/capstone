package com.lilamaris.capstone.scenario.auth.application.service;

import com.lilamaris.capstone.scenario.auth.application.exception.AuthenticationException;
import com.lilamaris.capstone.scenario.auth.application.port.in.CredentialAuthUseCase;
import com.lilamaris.capstone.scenario.auth.application.port.in.SessionIssuer;
import com.lilamaris.capstone.scenario.auth.application.port.out.*;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.shared.application.exception.ResourceAlreadyExistsException;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CredentialAuthService implements CredentialAuthUseCase {
    private final AuthAccountExistenceChecker accountExistenceChecker;
    private final AuthAccountRegistrar accountRegistrar;
    private final AuthUserRegistrar userRegistrar;
    private final AuthUserResolver userResolver;
    private final AuthVerifier authVerifier;
    private final SessionIssuer sessionIssuer;

    @Override
    @Transactional
    public AuthResult.Token register(String email, String passwordHash, String displayName) {
        if (accountExistenceChecker.isExists(AuthProvider.CREDENTIAL, email)) {
            throw new ResourceAlreadyExistsException("Account already exists.");
        }

        var userEntry = userRegistrar.register(displayName);
        accountRegistrar.register(userEntry.id(), email, passwordHash);

        return sessionIssuer.issue(userEntry.id(), userEntry.displayName());
    }

    @Override
    public AuthResult.Token signIn(String email, String passwordHash) {
        var verify = authVerifier.verify(AuthProvider.CREDENTIAL, email, passwordHash);

        if (!verify.success() || verify.entry() == null) {
            throw new AuthenticationException("Wrong credential.");
        }

        var userId = verify.entry().userId();
        var userEntry = userResolver.resolve(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "User with id '%s' not found.", userId
                )));

        return sessionIssuer.issue(userEntry.id(), userEntry.displayName());
    }
}
