package com.lilamaris.capstone.domain.user;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.embed.Audit;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record Account(
        Id id,
        User.Id userId,
        Provider provider,
        String providerId,
        String displayName,
        String email,
        String passwordHash,
        Audit audit
) implements BaseDomain<Account.Id, Account> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public Account {
        if (provider == null) throw new DomainIllegalArgumentException("Field 'provider' must not be null.");
        if (email == null) throw new DomainIllegalArgumentException("Field 'email' in the local provider based account must not be null.");

        if (provider.equals(Provider.LOCAL)) {
            if (passwordHash == null) throw new DomainIllegalArgumentException("Field 'passwordHash' in the local provider based account must not be null.");
            providerId = Provider.LOCAL.name() + ":" + email;
        } else {
            if (providerId == null) throw new DomainIllegalArgumentException("Field 'providerId' in the third party provider based account must not be null.");
        }

        id = Optional.ofNullable(id).orElseGet(Id::random);
    }

    public static Account from(Id id, User.Id userId, Provider provider, String providerId, String displayName, String email, String passwordHash, Audit audit) {
        return new Account(id, userId, provider, providerId, displayName, email, passwordHash, audit);
    }

    public static Account createCredential(String displayName, String email, String passwordHash) {
        var providerId = "local:" + email;
        return getDefaultBuilder()
                .provider(Provider.LOCAL)
                .providerId(providerId)
                .displayName(displayName)
                .email(email)
                .passwordHash(passwordHash)
                .build();
    }

    public static Account createOidc(Provider provider, String providerId, String email, String displayName) {
        return getDefaultBuilder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .displayName(displayName)
                .build();
    }

    public Account assignUser(User.Id userId) {
        return copyWithUserId(userId);
    }

    private Account copyWithUserId(User.Id userId) {
        return toBuilder().userId(userId).build();
    }
    private static AccountBuilder getDefaultBuilder() {
        return builder().id(Id.random());
    }
}
