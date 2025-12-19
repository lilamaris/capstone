package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.exception.ResourceAlreadyExistsException;
import com.lilamaris.capstone.application.port.in.AuthCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
import com.lilamaris.capstone.application.port.out.AuthPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.application.resolver.auth.*;
import com.lilamaris.capstone.domain.auth.RefreshToken;
import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.Provider;
import com.lilamaris.capstone.domain.user.Role;
import com.lilamaris.capstone.domain.user.User;
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

    private final AuthPort authPort;
    private final UserPort userPort;

    @Override
    @Transactional
    public AuthResult.Token refresh(String refreshToken) {
        var identity = tokenIdentityResolver.resolve(new RefreshToken.Id(refreshToken));
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
        if (authPort.isExists(Provider.LOCAL, email)) {
            throw new ResourceAlreadyExistsException("Account already exists.");
        }

        var account = Account.createCredential(displayName, email, passwordHash);
        var user = User.create(displayName, Role.USER);
        user = user.linkAccount(account);
        user = userPort.save(user);

        var identity = new AuthIdentity(user, account, true);
        return sessionIssuer.issue(identity);
    }
}
