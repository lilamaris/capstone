package com.lilamaris.capstone.shared.application.policy.access_control.defaults;

import com.lilamaris.capstone.shared.application.policy.access_control.port.in.ResourceAccessPolicy;
import com.lilamaris.capstone.shared.application.policy.access_control.port.in.ResourceAction;
import com.lilamaris.capstone.shared.application.policy.role.port.in.ResourceRole;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultResourceAccessPolicy implements
        ResourceAccessPolicy {
    private final Map<ResourceAction, Set<ResourceRole>> requiredByAction = new HashMap<>();
    private final DomainType supports;

    public void allow(ResourceAction action, Set<ResourceRole> roles) {
        requiredByAction.put(
                action,
                roles.stream().collect(Collectors.toUnmodifiableSet())
        );
    }

    @Override
    public Set<ResourceRole> requiredRoles(ResourceAction action) {
        return requiredByAction.getOrDefault(action, Set.of());
    }

    @Override
    public DomainType support() {
        return supports;
    }
}
