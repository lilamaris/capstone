package com.lilamaris.capstone.domain.model.auth.account;

import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.model.auth.account.id.AccountId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.embed.impl.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "account_root")
@EntityListeners(AuditingEntityListener.class)
public class Account implements Identifiable<AccountId> {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private AccountId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", updatable = false))
    private UserId userId;

    @Embedded
    private JpaAuditMetadata audit;

    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String providerId;
    private String displayName;
    private String email;
    private String passwordHash;

    protected Account(AccountId id, UserId userId, Provider provider, String providerId, String displayName, String email, String passwordHash) {
        this.id = requireField(id, "id");
        this.userId = requireField(userId, "userId");
        this.provider = requireField(provider, "provider");
        this.providerId = requireField(providerId, "providerId");
        this.displayName = requireField(displayName, "displayName");
        this.email = requireField(email, "email");
        this.passwordHash = passwordHash;
    }

    @Override
    public final AccountId id() {
        return id;
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
