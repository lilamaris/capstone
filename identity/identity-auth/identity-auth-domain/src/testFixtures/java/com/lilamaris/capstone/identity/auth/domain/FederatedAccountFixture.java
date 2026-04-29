package com.lilamaris.capstone.identity.auth.domain;

import java.time.Instant;

public class FederatedAccountFixture {
    public static final String INITIAL_REGISTRATION_ID = "google";
    public static final String INITIAL_PROVIDER_USER_ID = "google-user-1";

    public static FederatedAccount createFederatedAccount(Instant createdAt) {
        return FederatedAccount.of(
                UserFixture.createUser(createdAt),
                INITIAL_REGISTRATION_ID,
                INITIAL_PROVIDER_USER_ID,
                createdAt
        );
    }
}