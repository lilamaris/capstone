package com.lilamaris.capstone.application.policy.access_control;

import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;

public interface DomainPolicyDirectory {
    DomainPolicy<?> policyOf(DomainType domainType);
}
