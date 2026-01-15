package com.lilamaris.capstone.account.domain;

import com.lilamaris.capstone.account.domain.id.AccountId;
import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.user.domain.id.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class)
public class Account implements Persistable<AccountId>, Identifiable<AccountId>, Auditable {
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
    private ProviderType providerType;

    private String identityProvider;

    private String principalId;

    private String passwordHash;

    protected Account(
            AccountId id,
            UserId userId,
            ProviderType providerType,
            String identityProvider,
            String principalId,
            String passwordHash
    ) {
        this.id = requireField(id, "id");
        this.userId = requireField(userId, "userId");
        this.providerType = requireField(providerType, "providerType");
        this.identityProvider = requireField(identityProvider, "identityProvider");
        this.principalId = requireField(principalId, "principalId");
        this.passwordHash = passwordHash;
    }

    public static Account create(
            Supplier<AccountId> idSupplier,
            UserId userId,
            String provider,
            String email,
            String passwordHash
    ) {
        requireField(passwordHash, "passwordHash");

        return new Account(
                idSupplier.get(),
                userId,
                ProviderType.INTERNAL,
                provider,
                email,
                passwordHash
        );
    }

    public static Account create(
            Supplier<AccountId> idSupplier,
            UserId userId,
            String provider,
            String providerId
    ) {
        return new Account(
                idSupplier.get(),
                userId,
                ProviderType.EXTERNAL,
                provider,
                providerId,
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

    @Transient
    private boolean isNew = true;

    @Override
    public AccountId getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    private void markNotNew() {
        this.isNew = false;
    }
}
