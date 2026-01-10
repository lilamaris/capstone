package com.lilamaris.capstone.shared.application.policy.domain.role.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinitionDirectory;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleResolver;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DefaultDomainRoleResolver implements DomainRoleResolver {
    private final DomainRoleGraphDefinitionDirectory roleGraphDir;

    @Override
    public DomainRole ownerRoleOf(DomainType type) {
        return roleGraphDir.definitionOf(type).ownerRole();
    }

    @Override
    public DomainRole parse(DomainType type, String role) {
        return roleGraphDir.definitionOf(type).parse(role);
    }

    @Override
    public boolean atLeast(DomainType type, DomainRole role, DomainRole target) {
        if (role.equals(target)) return true;

        var hierarchy = roleGraphDir.definitionOf(type).hierarchy();

        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(role.name());

        while(!stack.isEmpty()) {
            var current = stack.pop();
            if (!visited.add(current)) continue;

            var parents = hierarchy.getOrDefault(current, Set.of());
            for (var parent: parents) {
                if (parent.equals(target.name())) {
                    return true;
                }

                stack.push(parent);
            }
        }

        return false;
    }
}
