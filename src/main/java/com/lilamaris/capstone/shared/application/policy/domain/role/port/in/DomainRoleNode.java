package com.lilamaris.capstone.shared.application.policy.domain.role.port.in;

import java.util.Set;

public record DomainRoleNode<R extends DomainRole>(
        R key,
        Set<R> parents
) {
}
