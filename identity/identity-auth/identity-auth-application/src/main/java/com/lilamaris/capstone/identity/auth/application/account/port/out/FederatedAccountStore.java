package com.lilamaris.capstone.identity.auth.application.account.port.out;

import com.lilamaris.capstone.identity.auth.domain.FederatedAccount;

public interface FederatedAccountStore {
    FederatedAccount save(FederatedAccount federatedAccount);

    void delete(FederatedAccount federatedAccount);
}
