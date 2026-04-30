package com.lilamaris.capstone.identity.auth.application.account.model;

import com.lilamaris.capstone.identity.auth.domain.account.CredentialAccount;
import com.lilamaris.capstone.identity.auth.domain.account.FederatedAccount;
import com.lilamaris.capstone.identity.auth.domain.account.User;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class UserAccountProvisioner {

    public CredentialAccountSet createCredentialUser(String nickname, String email, String passwordHash, Instant now) {
        var user = createUser(nickname, now);
        var account = CredentialAccount.of(user, email, passwordHash, now);

        return new CredentialAccountSet(user, account);
    }

    public FederatedAccountSet createFederatedUser(String nickname, String registrationId, String providerUserId, Instant now) {
        var user = createUser(nickname, now);
        var account = FederatedAccount.of(user, registrationId, providerUserId, now);

        return new FederatedAccountSet(user, account);
    }

    public FederatedAccountSet linkFederated(User user, String registrationId, String providerUserId, Instant now) {
        var account = FederatedAccount.of(user, registrationId, providerUserId, now);

        return new FederatedAccountSet(user, account);
    }

    private User createUser(String nickname, Instant createdAt) {
        Preconditions.requireNonBlank(nickname, "nickname");
        Preconditions.requireNonNull(createdAt, "createdAt");

        return User.of(nickname, createdAt);
    }

    public record CredentialAccountSet(User user, CredentialAccount account) {
    }

    public record FederatedAccountSet(User user, FederatedAccount account) {
    }
}
