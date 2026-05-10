package com.lilamaris.capstone.kernel.core.namespace;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public record FixedRunningNamespaceContext(
        ApplicationNamespace namespace
) implements RunningNamespaceContext {
    public FixedRunningNamespaceContext {
        Preconditions.requireNonNull(namespace, "namespace");
    }

    public static FixedRunningNamespaceContext of(ApplicationNamespace namespace) {
        return new FixedRunningNamespaceContext(namespace);
    }

    @Override
    public ApplicationNamespace get() {
        return null;
    }
}
