package com.lilamaris.capstone.identity.core.actor;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public record SimpleCapability(
        String scope,
        String description
) implements Capability {
    public SimpleCapability {
        scope = Preconditions.requireNonBlank(scope, "scope");
        description = Preconditions.requireNonBlank(description, "description");
    }

    public static SimpleCapability of(String scope, String description) {
        return new SimpleCapability(scope, description);
    }

    public static SimpleCapability from(Capability capability) {
        Preconditions.requireNonNull(capability, "capability");
        return of(capability.scope(), capability.description());
    }
}
