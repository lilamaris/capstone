package com.lilamaris.capstone.identity.auth.application.account.port.out;

import com.lilamaris.capstone.identity.auth.domain.account.CredentialAccount;

public interface CredentialAccountStore {
    CredentialAccount save(CredentialAccount credentialAccount);

    void delete(CredentialAccount credentialAccount);
}
