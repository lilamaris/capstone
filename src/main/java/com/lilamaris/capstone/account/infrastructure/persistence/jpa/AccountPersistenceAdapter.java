package com.lilamaris.capstone.account.infrastructure.persistence.jpa;

import com.lilamaris.capstone.account.application.port.out.AccountPort;
import com.lilamaris.capstone.account.domain.Account;
import com.lilamaris.capstone.account.domain.ProviderType;
import com.lilamaris.capstone.account.infrastructure.persistence.jpa.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountPort {
    private final AccountRepository repository;

    @Override
    public boolean isExists(ProviderType providerType, String identityProvider, String principalId) {
        return repository.existsByProviderTypeAndIdentityProviderAndPrincipalId(
                providerType,
                identityProvider,
                principalId
        );
    }

    @Override
    public Optional<Account> getBy(ProviderType providerType, String identityProvider, String principalId) {
        return repository.findByProviderTypeAndIdentityProviderAndPrincipalId(
                providerType,
                identityProvider,
                principalId
        );
    }

    @Override
    public Account save(Account account) {
        return repository.save(account);
    }
}
