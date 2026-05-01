package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import com.lilamaris.capstone.kernel.core.namespace.ApplicationNamespace;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderBasedInitialUserGrantedRoleRegistry implements InitialUserGrantedRoleRegistry {
    private final Map<String, NamespaceRole> registry;
    private final CanonicalRole fallbackRole;

    public ProviderBasedInitialUserGrantedRoleRegistry(
            List<InitialUserGrantedRoleProvider> providers,
            CanonicalRole fallbackRole
    ) {
        Preconditions.requireNonNull(providers, "providers");
        Preconditions.requireNonNull(fallbackRole, "fallbackRole");

        Map<String, NamespaceRole> initial = new HashMap<>();

        providers.forEach(provider -> {
            var namespaceRole = provider.provide();
            var key = keyOf(namespaceRole.namespace());
            if (initial.putIfAbsent(key, namespaceRole) != null) {
                throw new IllegalArgumentException("duplicated namespace found: " + key);
            }
        });

        this.registry = Map.copyOf(initial);
        this.fallbackRole = fallbackRole;
    }

    private static String keyOf(ApplicationNamespace namespace) {
        return namespace.name();
    }

    @Override
    public Collection<NamespaceRole> getAll() {
        return List.copyOf(registry.values());
    }

    @Override
    public NamespaceRole resolveByNamespace(ApplicationNamespace namespace) {
        Preconditions.requireNonNull(namespace, "namespace");
        return registry.getOrDefault(
                keyOf(namespace),
                SimpleNamespaceRole.of(namespace, fallbackRole)
        );
    }
}
