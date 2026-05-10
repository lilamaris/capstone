package com.lilamaris.capstone.kernel.core.namespace;

public interface RunningNamespaceContext {
    ApplicationNamespace get();

    default boolean isSame(ApplicationNamespace namespace) {
        return get().isSame(namespace);
    }
}
