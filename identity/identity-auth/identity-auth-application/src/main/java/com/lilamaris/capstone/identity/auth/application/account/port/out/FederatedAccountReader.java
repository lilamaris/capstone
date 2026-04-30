package com.lilamaris.capstone.identity.auth.application.account.port.out;

import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedProviderLookupCriteria;
import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedUserLookupCriteria;
import com.lilamaris.capstone.identity.auth.domain.account.FederatedAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FederatedAccountReader {
    boolean existsByCriteria(FederatedProviderLookupCriteria criteria);

    Optional<FederatedAccount> findById(UUID id);

    Optional<FederatedAccount> findByCriteria(FederatedUserLookupCriteria criteria);

    List<FederatedAccount> findByUserId(UUID userId);
}
