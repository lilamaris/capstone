package com.lilamaris.capstone.kernel.core.namespace;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public record SimpleApplicationNamespace(
        String name
) implements ApplicationNamespace {
    public SimpleApplicationNamespace {
        name = Preconditions.requireNonBlank(name, "name");
    }

    public static SimpleApplicationNamespace of(String name) {
        return new SimpleApplicationNamespace(name);
    }

    public static SimpleApplicationNamespace from(ApplicationNamespace namespace) {
        Preconditions.requireNonNull(namespace, "namespace");
        return of(namespace.name());
    }
}
