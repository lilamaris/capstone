package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.model.auth.account.Account;
import com.lilamaris.capstone.domain.model.auth.account.Provider;

import java.util.Optional;

public interface AccountPort {
    boolean isExists(Provider provider, String providerId);

    Optional<Account> getBy(Provider provider, String providerId);

    Account save(Account domain);
}
