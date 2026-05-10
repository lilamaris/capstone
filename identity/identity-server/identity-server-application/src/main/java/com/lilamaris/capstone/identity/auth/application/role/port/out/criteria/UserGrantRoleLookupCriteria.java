package com.lilamaris.capstone.identity.auth.application.role.port.out.criteria;

import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;

import java.util.UUID;

public record UserGrantRoleLookupCriteria(
        UUID userId,
        ApplicationNamespace namespace,
        CanonicalRole role
) {
    public UserGrantRoleLookupCriteria {
        Preconditions.requireNonNull(userId, "userId");
        Preconditions.requireNonNull(namespace, "namespace");
        Preconditions.requireNonNull(role, "role");
    }

    public static UserGrantRoleLookupCriteria of(UUID userId, ApplicationNamespace namespace, CanonicalRole role) {
        return new UserGrantRoleLookupCriteria(userId, namespace, role);
    }
}
