package com.lilamaris.capstone.adapter.out.persistence.entity;

import com.lilamaris.capstone.domain.user.Provider;
import com.lilamaris.capstone.domain.user.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "account")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountEntity extends BaseEntity<UUID> {
    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    private String providerId;
    private String email;
    private String displayName;
    private String passwordHash;
}
