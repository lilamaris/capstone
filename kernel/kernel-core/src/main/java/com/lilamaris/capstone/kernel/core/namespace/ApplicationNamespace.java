package com.lilamaris.capstone.kernel.core.namespace;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public interface ApplicationNamespace {
    String name();

    default boolean isSame(ApplicationNamespace namespace) {
        Preconditions.requireNonNull(namespace, "namespace");
        return name().equals(namespace.name());
    }
}
