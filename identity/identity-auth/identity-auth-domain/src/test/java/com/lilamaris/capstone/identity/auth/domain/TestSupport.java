package com.lilamaris.capstone.identity.auth.domain;

import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;

public final class TestSupport {
    public static final String INITIAL_NAMESPACE_NAME = "test-namespace";

    private TestSupport() {
    }

    public static ApplicationNamespace createApplicationNamespace() {
        return createApplicationNamespace(INITIAL_NAMESPACE_NAME);
    }

    public static ApplicationNamespace createApplicationNamespace(String name) {
        return SimpleApplicationNamespace.of(name);
    }
}
