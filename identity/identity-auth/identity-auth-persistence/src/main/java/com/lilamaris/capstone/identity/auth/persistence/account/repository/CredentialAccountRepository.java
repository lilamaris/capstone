package com.lilamaris.capstone.identity.auth.persistence.account.repository;

import com.lilamaris.capstone.identity.auth.domain.account.CredentialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialAccountRepository extends JpaRepository<CredentialAccount, UUID> {
    boolean existsByEmail(String email);

    Optional<CredentialAccount> findByEmail(String email);

    Optional<CredentialAccount> findByUserId(UUID userId);
}
