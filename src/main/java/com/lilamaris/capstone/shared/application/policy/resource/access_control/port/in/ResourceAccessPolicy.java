package com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.Set;

public interface ResourceAccessPolicy {
    Set<DomainRole> requiredRoles(ResourceAction action);

    DomainType support();
}
