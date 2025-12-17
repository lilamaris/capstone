package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.adapter.out.jpa.entity.AccountEntity;
import com.lilamaris.capstone.domain.user.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    boolean existsByProviderAndProviderId(Provider provider, String providerId);
    Optional<AccountEntity> findByProviderAndProviderId(Provider provider, String providerId);
}
