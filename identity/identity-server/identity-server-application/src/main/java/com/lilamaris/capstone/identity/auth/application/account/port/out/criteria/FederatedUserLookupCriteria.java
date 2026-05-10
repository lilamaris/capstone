package com.lilamaris.capstone.identity.auth.application.account.port.out.criteria;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.UUID;

public record FederatedUserLookupCriteria(
        UUID userId,
        String registrationId
) {
    public FederatedUserLookupCriteria {
        Preconditions.requireNonNull(userId, "userId");
        Preconditions.requireNonBlank(registrationId, "registrationId");
    }

    public static FederatedUserLookupCriteria of(UUID userId, String registrationId) {
        return new FederatedUserLookupCriteria(userId, registrationId);
    }
}
