package com.lilamaris.capstone.shared.application.access_control.policy.defaults;

import com.lilamaris.capstone.shared.application.access_control.contract.DomainAction;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainPolicy;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainRole;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultPolicy<R extends Enum<R> & DomainRole> implements DomainPolicy<R> {
    private final Map<String, Set<String>> bindings = new HashMap<>();
    private final DomainType supports;
    private final Class<R> supportRole;

    public void extend(DomainRole child, DomainRole parent) {
        var parentActions = bindings.getOrDefault(parent.name(), Set.of());
        bindings
                .computeIfAbsent(child.name(), k -> new HashSet<>())
                .addAll(parentActions);
    }

    public void allow(DomainRole role, Set<DomainAction> actions) {
        bindings.put(
                role.name(),
                actions.stream()
                        .map(DomainAction::name)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public boolean allows(DomainRole role, DomainAction action) {
        if (role == null || action == null) return false;
        var actions = bindings.get(role.name());
        return actions != null && actions.contains(action.name());
    }

    @Override
    public R parseRole(String scopedRole) {
        return Enum.valueOf(supportRole, scopedRole);
    }

    @Override
    public DomainType supportType() {
        return supports;
    }
}
