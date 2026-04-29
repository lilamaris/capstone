package com.lilamaris.capstone.identity.auth.domain;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "credential_account",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_credential_account_email",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "uk_credential_account_user_id",
                        columnNames = "user_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CredentialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    private CredentialAccount(User user, String email, String passwordHash, Instant createdAt) {
        this.user = Preconditions.requireNonNull(user, "user");
        this.email = Preconditions.requireNonBlank(email, "email");
        this.passwordHash = Preconditions.requireNonBlank(passwordHash, "passwordHash");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public static CredentialAccount of(User user, String email, String passwordHash, Instant createdAt) {
        return new CredentialAccount(user, email, passwordHash, createdAt);
    }

    public void updatePasswordHash(String passwordHash) {
        this.passwordHash = Preconditions.requireNonBlank(passwordHash, "passwordHash");
    }
}
