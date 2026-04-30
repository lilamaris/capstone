package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;

public record SimpleNamespaceRole(
        ApplicationNamespace namespace,
        CanonicalRole role
) implements NamespaceRole {
    public SimpleNamespaceRole {
        Preconditions.requireNonNull(namespace, "namespace");
        Preconditions.requireNonNull(role, "role");
    }

    public static SimpleNamespaceRole of(ApplicationNamespace namespace, CanonicalRole role) {
        return new SimpleNamespaceRole(namespace, role);
    }

    public static SimpleNamespaceRole from(NamespaceRole namespaceRole) {
        Preconditions.requireNonNull(namespaceRole, "namespaceRole");
        return of(namespaceRole.namespace(), namespaceRole.role());
    }
}
