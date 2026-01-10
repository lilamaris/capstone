package com.lilamaris.capstone.shared.application.policy.domain.role.port.in;

import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.Map;
import java.util.Set;

public interface DomainRoleGraphDefinition<R extends DomainRole> {
    DomainType support();

    Map<String, Set<String>> hierarchy();

    R ownerRole();

    R parse(String role);
}
