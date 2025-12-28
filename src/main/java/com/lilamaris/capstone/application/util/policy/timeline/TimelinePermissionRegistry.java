package com.lilamaris.capstone.application.util.policy.timeline;

import com.lilamaris.capstone.application.util.policy.DomainAction;
import com.lilamaris.capstone.application.util.policy.DomainPermissionRegistry;
import com.lilamaris.capstone.application.util.policy.DomainRole;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TimelinePermissionRegistry implements DomainPermissionRegistry {
    private final Map<String, Set<String>> bindings = new HashMap<>();

    public void extend(DomainRole child, DomainRole parent) {
        var parentActions = bindings.getOrDefault(parent.name(), Set.of());
        bindings
                .computeIfAbsent(child.name(), k -> new HashSet<>())
                .addAll(parentActions);
    }

    @Override
    public void register(DomainRole role, Set<DomainAction> actions) {
        bindings.put(
                role.name(),
                actions.stream()
                        .map(DomainAction::name)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public boolean can(DomainRole role, DomainAction action) {
        if (role == null || action == null) return false;
        var actions = bindings.get(role.name());
        return actions != null && actions.contains(action.name());
    }
}
