package com.lilamaris.capstone.identity.auth.domain;

import com.lilamaris.capstone.identity.core.actor.CanonicalRole;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "user_granted_role",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_granted_role_user_id_namespace_role",
                        columnNames = {"user_id", "namespace", "role"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGrantedRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "namespace", nullable = false))
    private EmbeddableApplicationNamespace namespace;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private CanonicalRole role;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    private UserGrantedRole(User user, EmbeddableApplicationNamespace namespace, CanonicalRole role, Instant createdAt) {
        this.user = Preconditions.requireNonNull(user, "user");
        this.namespace = Preconditions.requireNonNull(namespace, "namespace");
        this.role = Preconditions.requireNonNull(role, "role");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public static UserGrantedRole of(User user, ApplicationNamespace namespace, CanonicalRole role, Instant createdAt) {
        return new UserGrantedRole(
                user,
                EmbeddableApplicationNamespace.from(namespace),
                role,
                createdAt
        );
    }
}