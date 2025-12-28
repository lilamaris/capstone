package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.exception.ResourceAlreadyExistsException;
import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import com.lilamaris.capstone.application.port.out.AccountPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.application.resolver.auth.*;
import com.lilamaris.capstone.domain.model.auth.account.Account;
import com.lilamaris.capstone.domain.model.auth.account.Provider;
import com.lilamaris.capstone.domain.model.auth.account.id.AccountId;
import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;
import com.lilamaris.capstone.domain.model.capstone.user.Role;
import com.lilamaris.capstone.domain.model.capstone.user.User;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthCommandService implements AuthCommandUseCase {
    private final CredentialIdentityResolver credentialIdentityResolver;
    private final OidcIdentityResolver oidcIdentityResolver;
    private final TokenIdentityResolver tokenIdentityResolver;
    private final SessionIssuer sessionIssuer;

    private final IdGenerationContext ids;
    private final AccountPort accountPort;
    private final UserPort userPort;

    @Override
    @Transactional
    public AuthResult.Token refresh(String refreshToken) {
        var identity = tokenIdentityResolver.resolve(new RefreshTokenId(refreshToken));
        return sessionIssuer.issue(identity);

    }

    @Override
    @Transactional
    public AuthResult.Token oidcSignIn(Provider provider, String providerId, String email, String displayName) {
        var identity = oidcIdentityResolver.resolve(provider, providerId, email, displayName);
        return sessionIssuer.issue(identity);
    }

    @Override
    @Transactional
    public AuthResult.Token credentialSignIn(String email, Function<String, Boolean> challengeFunction) {
        var identity = credentialIdentityResolver.resolve(email, challengeFunction);
        return sessionIssuer.issue(identity);
    }

    @Override
    public AuthResult.Token credentialRegister(String email, String passwordHash, String displayName) {
        if (accountPort.isExists(Provider.LOCAL, email)) {
            throw new ResourceAlreadyExistsException("Account already exists.");
        }

        var user = User.create(displayName, Role.USER, () -> ids.next(UserId.class));
        var account = Account.create(user.id(), displayName, email, passwordHash, () -> ids.next(AccountId.class));

        user = userPort.save(user);
        account = accountPort.save(account);

        var identity = new AuthIdentity(user, account, true);
        return sessionIssuer.issue(identity);
    }
}
