package com.lilamaris.capstone.identity.core.role;

import java.util.Collection;
import java.util.Set;

public interface NamespaceRoleSerializer {
    String serialize(NamespaceRole source);

    Set<String> serialize(Collection<NamespaceRole> sources);
}
