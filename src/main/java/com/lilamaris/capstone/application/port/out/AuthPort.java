package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.Provider;

import java.util.Optional;

public interface AuthPort {
    Optional<Account> getBy(Provider provider, String providerId);
}
