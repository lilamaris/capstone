package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;

public class NamespaceRoleFixture {
    public static final String INITIAL_NAMESPACE_NAME = "test-namespace";
    public static final CanonicalRole INITIAL_ROLE = CanonicalRole.USER;
    public static final ApplicationNamespace INITIAL_NAMESPACE = SimpleApplicationNamespace.of(INITIAL_NAMESPACE_NAME);

    public static NamespaceRole createNamespaceRole() {
        return SimpleNamespaceRole.of(INITIAL_NAMESPACE, INITIAL_ROLE);
    }

    public static NamespaceRole createNamespaceRole(String namespaceName, CanonicalRole role) {
        return SimpleNamespaceRole.of(SimpleApplicationNamespace.of(namespaceName), role);
    }

    public static NamespaceRole createNamespaceRole(ApplicationNamespace namespace, CanonicalRole role) {
        return SimpleNamespaceRole.of(namespace, role);
    }

    public static NamespaceRoleBuilder builder() {
        return new NamespaceRoleBuilder();
    }

    public static class NamespaceRoleBuilder {
        private ApplicationNamespace namespace = INITIAL_NAMESPACE;
        private CanonicalRole role = INITIAL_ROLE;

        public NamespaceRoleBuilder namespace(ApplicationNamespace namespace) {
            this.namespace = namespace;
            return this;
        }

        public NamespaceRoleBuilder role(CanonicalRole role) {
            this.role = role;
            return this;
        }

        public NamespaceRole build() {
            return createNamespaceRole(namespace, role);
        }
    }
}
