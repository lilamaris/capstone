package com.lilamaris.capstone.identity.auth.application.account.port.out.criteria;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public record FederatedProviderLookupCriteria(
        String registrationId,
        String providerUserId
) {
    public FederatedProviderLookupCriteria {
        Preconditions.requireNonBlank(registrationId, "registrationId");
        Preconditions.requireNonBlank(providerUserId, "providerUserId");
    }

    public static FederatedProviderLookupCriteria of(String registrationId, String providerUserId) {
        return new FederatedProviderLookupCriteria(registrationId, providerUserId);
    }
}
