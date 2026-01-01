package com.lilamaris.capstone.account.application.port.out;

import com.lilamaris.capstone.account.domain.Account;
import com.lilamaris.capstone.account.domain.Provider;

import java.util.Optional;

public interface AccountPort {
    boolean isExists(Provider provider, String providerId);

    Optional<Account> getBy(Provider provider, String providerId);

    Account save(Account domain);
}
