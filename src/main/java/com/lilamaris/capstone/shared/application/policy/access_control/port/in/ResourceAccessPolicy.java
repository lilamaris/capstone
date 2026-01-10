package com.lilamaris.capstone.shared.application.policy.access_control.port.in;

import com.lilamaris.capstone.shared.application.policy.role.port.in.ResourceRole;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.Set;

public interface ResourceAccessPolicy {
    Set<ResourceRole> requiredRoles(ResourceAction action);

    DomainType support();
}
