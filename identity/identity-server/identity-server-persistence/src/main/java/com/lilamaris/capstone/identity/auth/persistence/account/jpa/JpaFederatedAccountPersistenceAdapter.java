package com.lilamaris.capstone.identity.auth.persistence.account.jpa;

import com.lilamaris.capstone.identity.auth.application.account.port.out.FederatedAccountReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.FederatedAccountStore;
import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedProviderLookupCriteria;
import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedUserLookupCriteria;
import com.lilamaris.capstone.identity.auth.domain.account.FederatedAccount;
import com.lilamaris.capstone.identity.auth.persistence.account.jpa.repository.FederatedAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaFederatedAccountPersistenceAdapter implements FederatedAccountStore, FederatedAccountReader {
    private final FederatedAccountRepository repository;

    @Override
    public boolean existsByCriteria(FederatedProviderLookupCriteria criteria) {
        return repository.existsByRegistrationIdAndProviderUserId(criteria.registrationId(), criteria.providerUserId());
    }

    @Override
    public Optional<FederatedAccount> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<FederatedAccount> findByCriteria(FederatedUserLookupCriteria criteria) {
        return repository.findByUserIdAndRegistrationId(criteria.userId(), criteria.registrationId());
    }

    @Override
    public List<FederatedAccount> findByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public FederatedAccount save(FederatedAccount federatedAccount) {
        return repository.save(federatedAccount);
    }

    @Override
    public void delete(FederatedAccount federatedAccount) {
        repository.delete(federatedAccount);
    }
}
