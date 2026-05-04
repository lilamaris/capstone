package com.lilamaris.capstone.identity.auth.persistence.account.jpa;

import com.lilamaris.capstone.identity.auth.application.account.port.out.CredentialAccountReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.CredentialAccountStore;
import com.lilamaris.capstone.identity.auth.domain.account.CredentialAccount;
import com.lilamaris.capstone.identity.auth.persistence.account.jpa.repository.CredentialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaCredentialAccountPersistenceAdapter implements CredentialAccountStore, CredentialAccountReader {
    private final CredentialAccountRepository repository;

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<CredentialAccount> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<CredentialAccount> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<CredentialAccount> findByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public CredentialAccount save(CredentialAccount credentialAccount) {
        return repository.save(credentialAccount);
    }

    @Override
    public void delete(CredentialAccount credentialAccount) {
        repository.delete(credentialAccount);
    }
}
