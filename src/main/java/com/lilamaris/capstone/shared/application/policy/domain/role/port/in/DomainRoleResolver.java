package com.lilamaris.capstone.shared.application.policy.domain.role.port.in;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainRoleResolver {
    DomainRole ownerRoleOf(DomainType type);

    DomainRole parse(DomainType type, String role);

    boolean atLeast(
            DomainType type,
            DomainRole role,
            DomainRole target
    );
}
