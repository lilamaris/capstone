package com.lilamaris.capstone.shared.application.policy.role.port.in;

import java.util.Set;

public record RoleNode<R extends ResourceRole>(
        R key,
        Set<R> parents
) {
}
