package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.application.port.out.AccountPort;
import com.lilamaris.capstone.domain.model.auth.account.Account;
import com.lilamaris.capstone.domain.model.auth.account.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, AccountPort {
    boolean existsByProviderAndProviderId(Provider provider, String providerId);

    Optional<Account> findByProviderAndProviderId(Provider provider, String providerId);

    @Override
    default boolean isExists(Provider provider, String providerId) {
        return existsByProviderAndProviderId(provider, providerId);
    }

    @Override
    default Optional<Account> getBy(Provider provider, String providerId) {
        return findByProviderAndProviderId(provider, providerId);
    }
}
