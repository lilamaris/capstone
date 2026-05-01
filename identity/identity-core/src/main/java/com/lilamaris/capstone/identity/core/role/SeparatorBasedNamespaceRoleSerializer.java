package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SeparatorBasedNamespaceRoleSerializer implements NamespaceRoleSerializer {
    public static final String SEPARATOR = ":";

    @Override
    public String serialize(NamespaceRole source) {
        Preconditions.requireNonNull(source, "source");

        var namespace = source.namespace();
        var role = source.role();
        ensureNotContainSeparator(namespace);

        return namespace.name() + SEPARATOR + role.name();
    }

    @Override
    public Set<String> serialize(Collection<NamespaceRole> sources) {
        Preconditions.requireNonNull(sources, "sources");

        return sources.stream()
                .map(this::serialize)
                .collect(Collectors.toUnmodifiableSet());
    }

    private void ensureNotContainSeparator(ApplicationNamespace namespace) {
        if (namespace.name().contains(SEPARATOR)) {
            throw new IllegalArgumentException(
                    "namespace value must not contain separator '%s': '%s'."
                            .formatted(SEPARATOR, namespace)
            );
        }
    }
}
