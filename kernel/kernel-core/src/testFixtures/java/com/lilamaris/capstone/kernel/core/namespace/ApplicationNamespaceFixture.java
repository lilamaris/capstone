package com.lilamaris.capstone.kernel.core.namespace;

public class ApplicationNamespaceFixture {
    public static final String INITIAL_NAMESPACE_NAME = "test-namespace";

    public static ApplicationNamespace createApplicationNamespace() {
        return SimpleApplicationNamespace.of(INITIAL_NAMESPACE_NAME);
    }

    public static ApplicationNamespace createApplicationNamespace(String name) {
        return SimpleApplicationNamespace.of(name);
    }
}
