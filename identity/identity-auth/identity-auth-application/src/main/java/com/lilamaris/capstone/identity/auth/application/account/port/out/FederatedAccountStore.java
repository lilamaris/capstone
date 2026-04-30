package com.lilamaris.capstone.identity.auth.application.account.port.out;

import com.lilamaris.capstone.identity.auth.domain.account.FederatedAccount;

public interface FederatedAccountStore {
    FederatedAccount save(FederatedAccount federatedAccount);

    void delete(FederatedAccount federatedAccount);
}
