package com.lilamaris.capstone.identity.core.role;

import com.lilamaris.capstone.identity.core.actor.Capability;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRoleCapabilityRegistry implements RoleCapabilityResolver {
    private final Map<CanonicalRole, Set<Capability>> registry;

    public InMemoryRoleCapabilityRegistry(List<RoleCapabilities> roleCapabilities) {
        Preconditions.requireNonNull(roleCapabilities, "roleCapabilities");

        var capabilitiesByRole = new EnumMap<CanonicalRole, Set<Capability>>(CanonicalRole.class);

        for (var rc : roleCapabilities) {
            var previous = capabilitiesByRole.putIfAbsent(rc.role(), rc.capabilities());
            if (previous != null)
                throw new IllegalArgumentException("Duplicated role capabilities. role=" + rc.role().name());
        }

        Map<CanonicalRole, Set<Capability>> registry = new HashMap<>();
        var prefixSum = new HashSet<Capability>();
        for (var role : CanonicalRole.rankOrder()) {
            prefixSum.addAll(capabilitiesByRole.getOrDefault(role, Set.of()));
            registry.put(role, Set.copyOf(prefixSum));
        }

        this.registry = Map.copyOf(registry);
    }

    @Override
    public Set<Capability> resolve(CanonicalRole role) {
        Preconditions.requireNonNull(role, "role");
        var capabilities = registry.get(role);
        if (capabilities == null)
            throw new IllegalStateException("Role capability is not registered. role=" + role.name());
        return capabilities;
    }

    @Override
    public Set<Capability> resolve(Collection<CanonicalRole> roles) {
        Preconditions.requireNonNull(roles, "roles");
        return roles.stream()
                .flatMap(role -> registry.getOrDefault(role, Set.of()).stream())
                .collect(Collectors.toUnmodifiableSet());
    }
}
