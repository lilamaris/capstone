package com.lilamaris.capstone.shared.application.policy.role.port.in;

import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.Map;
import java.util.Set;

public interface RoleGraphDefinition<R extends ResourceRole> {
    DomainType support();

    Map<String, Set<String>> hierarchy();

    R ownerRole();

    R parse(String role);
}
