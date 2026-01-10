package com.lilamaris.capstone.shared.application.policy.role.port.in;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface RoleResolver {
    ResourceRole ownerRoleOf(DomainType type);

    ResourceRole parse(DomainType type, String role);

    boolean atLeast(
            DomainType type,
            ResourceRole role,
            ResourceRole target
    );
}
