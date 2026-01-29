package com.lilamaris.capstone.auth.account.application.port.out;

import com.lilamaris.capstone.auth.account.domain.Account;
import com.lilamaris.capstone.auth.account.domain.ProviderType;

import java.util.Optional;

public interface AccountPort {
    boolean isExists(ProviderType providerType, String identityProvider, String principalId);

    Optional<Account> getBy(ProviderType providerType, String identityProvider, String principalId);

    Account save(Account account);
}
