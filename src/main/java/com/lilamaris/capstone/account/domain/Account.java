package com.lilamaris.capstone.account.domain;

import com.lilamaris.capstone.account.domain.id.AccountId;
import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.user.domain.id.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class)
public class Account implements Identifiable<AccountId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private AccountId id;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", updatable = false))
    private UserId userId;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String providerId;
    private String displayName;
    private String email;
    private String passwordHash;

    protected Account(
            AccountId id,
            UserId userId,
            Provider provider,
            String providerId,
            String displayName,
            String email,
            String passwordHash
    ) {
        this.id = requireField(id, "id");
        this.userId = requireField(userId, "userId");
        this.provider = requireField(provider, "provider");
        this.providerId = requireField(providerId, "providerId");
        this.displayName = requireField(displayName, "displayName");
        this.email = requireField(email, "email");
        this.passwordHash = passwordHash;
    }

    public static Account create(
            UserId userId,
            String displayName,
            String email,
            String passwordHash,
            Supplier<AccountId> idSupplier
    ) {
        return new Account(
                idSupplier.get(),
                userId,
                Provider.LOCAL,
                email,
                displayName,
                email,
                requireField(passwordHash, "passwordHash")
        );
    }

    public static Account create(
            UserId userId,
            Provider provider,
            String providerId,
            String displayName,
            String email,
            Supplier<AccountId> idSupplier
    ) {
        if (provider.equals(Provider.LOCAL)) {
            throw new DomainIllegalArgumentException(
                    "Can not create a local account by explicitly specifying the provider."
            );
        }

        return new Account(
                idSupplier.get(),
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

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }

    public boolean challengeHash(String rawPassword, PasswordEncoder encoder) {
        if (!provider.equals(Provider.LOCAL)) {
            throw new DomainIllegalArgumentException(
                    "Can not perform challenge hash in not local provider."
            );
        }

        return encoder.matches(rawPassword, passwordHash);
    }
}
