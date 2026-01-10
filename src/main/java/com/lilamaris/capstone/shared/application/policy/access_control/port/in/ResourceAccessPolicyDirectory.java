package com.lilamaris.capstone.shared.application.policy.access_control.port.in;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface ResourceAccessPolicyDirectory {
    ResourceAccessPolicy policyOf(DomainType domainType);
}
