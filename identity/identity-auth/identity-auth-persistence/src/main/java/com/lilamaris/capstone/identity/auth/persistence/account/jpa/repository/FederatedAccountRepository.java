package com.lilamaris.capstone.identity.auth.persistence.account.jpa.repository;

import com.lilamaris.capstone.identity.auth.domain.account.FederatedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FederatedAccountRepository extends JpaRepository<FederatedAccount, UUID> {
    boolean existsByRegistrationIdAndProviderUserId(String registrationId, String providerUserId);

    Optional<FederatedAccount> findByUserIdAndRegistrationId(UUID userId, String registrationId);

    List<FederatedAccount> findByUserId(UUID userId);
}
