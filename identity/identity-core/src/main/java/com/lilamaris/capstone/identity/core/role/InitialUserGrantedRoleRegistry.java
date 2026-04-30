package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;

import java.util.Collection;

public interface InitialUserGrantedRoleRegistry {
    Collection<NamespaceRole> getAll();

    NamespaceRole resolveByNamespace(ApplicationNamespace namespace);
}
