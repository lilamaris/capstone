package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;

public interface NamespaceRole {
    ApplicationNamespace namespace();

    CanonicalRole role();

    default boolean isSame(NamespaceRole namespaceRole) {
        Preconditions.requireNonNull(namespaceRole, "namespaceRole");
        return namespace().isSame(namespaceRole.namespace()) && role() == namespaceRole.role();
    }
}
