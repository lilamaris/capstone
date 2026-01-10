package com.lilamaris.capstone.scenario.auth.application.service;

import com.lilamaris.capstone.account.application.port.out.AccountPort;
import com.lilamaris.capstone.account.domain.Account;
import com.lilamaris.capstone.account.domain.Provider;
import com.lilamaris.capstone.account.domain.id.AccountId;
import com.lilamaris.capstone.scenario.auth.application.port.in.CredentialAuthUseCase;
import com.lilamaris.capstone.scenario.auth.application.resolver.AuthIdentity;
import com.lilamaris.capstone.scenario.auth.application.resolver.CredentialIdentityResolver;
import com.lilamaris.capstone.scenario.auth.application.resolver.SessionIssuer;
import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.shared.application.exception.ResourceAlreadyExistsException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.user.application.port.out.UserPort;
import com.lilamaris.capstone.user.domain.Role;
import com.lilamaris.capstone.user.domain.User;
import com.lilamaris.capstone.user.domain.id.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CredentialAuthService implements CredentialAuthUseCase {
    private final CredentialIdentityResolver credentialIdentityResolver;
    private final SessionIssuer sessionIssuer;

    private final IdGenerationDirectory ids;
    private final AccountPort accountPort;
    private final UserPort userPort;

    @Override
    public AuthResult.Token register(String email, String passwordHash, String displayName) {
        if (accountPort.isExists(Provider.LOCAL, email)) {
            throw new ResourceAlreadyExistsException("Account already exists.");
        }

        var user = User.create(displayName, Role.USER, ids.next(UserId.class));
        var account = Account.create(user.id(), displayName, email, passwordHash, ids.next(AccountId.class));

        user = userPort.save(user);
        account = accountPort.save(account);

        var identity = new AuthIdentity(user, account, true);
        return sessionIssuer.issue(identity);
    }

    @Override
    @Transactional
    public AuthResult.Token signIn(String email, Function<String, Boolean> challengeFunction) {
        var identity = credentialIdentityResolver.resolve(email, challengeFunction);
        return sessionIssuer.issue(identity);
    }
}
