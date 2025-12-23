package com.lilamaris.capstone.domain.model.auth;

import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultAuditableDomain;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.model.auth.id.AccountId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.user.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.util.function.BiFunction;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@Entity
@Table(name = "account_root")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends JpaDefaultAuditableDomain implements Identifiable<AccountId> {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    private AccountId id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "timeline_id", insertable = false, updatable = false))
    private UserId userId;

    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String providerId;
    private String displayName;
    private String email;
    private String passwordHash;

    public static String getLocalProviderId(String email) {
        return Provider.LOCAL.name() + ":" + email;
    }

    public static Account create(UserId userId, String displayName, String email, String passwordHash) {
        var providerId = getLocalProviderId(email);
        return new Account(
                AccountId.newId(),
                userId,
                Provider.LOCAL,
                providerId,
                displayName,
                email,
                requireField(passwordHash, "passwordHash")
        );
    }

    public static Account create(UserId userId, Provider provider, String providerId, String displayName, String email) {
        if (provider.equals(Provider.LOCAL)) {
            throw new DomainIllegalArgumentException(
                    "Can not create a local account by explicitly specifying the provider."
            );
        }
        return new Account(
                AccountId.newId(),
                userId,
                provider,
                providerId,
                displayName,
                email,
                null
        );
    }

    @Override
    public final AccountId id() {
        return id;
    }

    public boolean challengeHash(String rawPassword, BiFunction<String, String, Boolean> challenge) {
        if (!provider.equals(Provider.LOCAL)) {
            throw new DomainIllegalArgumentException(
                    "Can not perform challenge hash in not local provider."
            );
        }
        return challenge.apply(passwordHash, rawPassword);
    }

    private Account(AccountId id, UserId userId, Provider provider, String providerId, String displayName, String email, String passwordHash) {
        this.id             = requireField(id, "id");
        this.userId         = requireField(userId, "userId");
        this.provider       = requireField(provider, "provider");
        this.providerId     = requireField(providerId, "providerId");
        this.displayName    = requireField(displayName, "displayName");
        this.email          = requireField(email, "email");
        this.passwordHash   = passwordHash;
    }
}
