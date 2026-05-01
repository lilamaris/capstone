package com.lilamaris.capstone.identity.core.role;

import java.util.Collection;
import java.util.Set;

public interface NamespaceRoleDeserializer {
    NamespaceRole deserialize(String source);

    Set<NamespaceRole> deserialize(Collection<String> sources);
}