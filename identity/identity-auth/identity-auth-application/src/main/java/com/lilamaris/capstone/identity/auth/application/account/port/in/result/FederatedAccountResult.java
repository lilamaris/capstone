package com.lilamaris.capstone.identity.auth.application.account.port.in.result;

import com.lilamaris.capstone.identity.auth.domain.account.FederatedAccount;

import java.time.Instant;

public record FederatedAccountResult(
        String registrationId,
        Instant createdAt
) {
    public static FederatedAccountResult from(FederatedAccount account) {
        return new FederatedAccountResult(
                account.getRegistrationId(),
                account.getCreatedAt()
        );
    }
}
