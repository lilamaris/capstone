package com.lilamaris.capstone.identity.core.actor;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public interface Capability {
    String scope();

    String description();

    default boolean isSame(Capability capability) {
        Preconditions.requireNonNull(capability, "capability");
        return scope().equals(capability.scope());
    }
}
