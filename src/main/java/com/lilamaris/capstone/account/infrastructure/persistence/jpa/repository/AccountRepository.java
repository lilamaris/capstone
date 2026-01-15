package com.lilamaris.capstone.account.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.account.domain.Account;
import com.lilamaris.capstone.account.domain.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByProviderTypeAndIdentityProviderAndPrincipalId(ProviderType providerType, String identityProvider, String providerId);

    Optional<Account> findByProviderTypeAndIdentityProviderAndPrincipalId(ProviderType providerType, String identityProvider, String principalId);
}
