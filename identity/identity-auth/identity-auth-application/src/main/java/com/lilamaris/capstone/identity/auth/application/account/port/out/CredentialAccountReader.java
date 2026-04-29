package com.lilamaris.capstone.identity.auth.application.account.port.out;

import com.lilamaris.capstone.identity.auth.domain.CredentialAccount;

import java.util.Optional;
import java.util.UUID;

public interface CredentialAccountReader {
    boolean existsByEmail(String email);

    Optional<CredentialAccount> findById(UUID id);

    Optional<CredentialAccount> findByEmail(String email);

    Optional<CredentialAccount> findByUserId(UUID userId);
}
