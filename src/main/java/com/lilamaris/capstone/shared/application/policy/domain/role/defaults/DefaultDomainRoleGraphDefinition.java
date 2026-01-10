package com.lilamaris.capstone.shared.application.policy.domain.role.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinition;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultDomainRoleGraphDefinition<R extends Enum<R> & DomainRole> implements DomainRoleGraphDefinition<R> {
    private final Map<String, Set<String>> graph = new HashMap<>();
    private final DomainType type;
    private final Class<R> roleClass;
    private R ownerRole;

    public void extend(R child, R parent) {
        graph.computeIfAbsent(child.name(), k -> new HashSet<>()).add(parent.name());
        graph.computeIfAbsent(parent.name(), k -> new HashSet<>());
    }

    public void setOwner(R role) {
        this.ownerRole = role;
    }

    @Override
    public Map<String, Set<String>> hierarchy() {
        return Map.copyOf(graph);
    }

    @Override
    public R parse(String role) {
        return Enum.valueOf(roleClass, role);
    }

    @Override
    public R ownerRole() {
        return ownerRole;
    }

    @Override
    public DomainType support() {
        return type;
    }

}
