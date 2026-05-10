package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.identity.core.actor.Capability;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.Set;

public record RoleCapabilities(
        CanonicalRole role,
        Set<Capability> capabilities
) {
    public RoleCapabilities {
        Preconditions.requireNonNull(role, "role");
        Preconditions.requireNonNull(capabilities, "capabilities");
    }

    public static RoleCapabilities of(CanonicalRole role, Set<Capability> capabilities) {
        return new RoleCapabilities(role, capabilities);
    }
}
