package com.lilamaris.capstone.identity.core.actor;

public interface Capability {
    String scope();

    String description();

    default boolean isSame(Capability capability) {
        return scope().equals(capability.scope());
    }
}
