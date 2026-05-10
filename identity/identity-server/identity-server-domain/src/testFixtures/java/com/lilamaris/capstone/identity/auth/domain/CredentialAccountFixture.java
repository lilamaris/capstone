package com.lilamaris.capstone.identity.auth.domain;

import com.lilamaris.capstone.identity.auth.domain.account.CredentialAccount;

import java.time.Instant;

public class CredentialAccountFixture {
    public static final String INITIAL_EMAIL = "tester@example.com";
    public static final String INITIAL_PASSWORD_HASH = "{bcrypt}password-hash";

    public static CredentialAccount createCredentialAccount(Instant createdAt) {
        return CredentialAccount.of(
                UserFixture.createUser(createdAt),
                INITIAL_EMAIL,
                INITIAL_PASSWORD_HASH,
                createdAt
        );
    }
}