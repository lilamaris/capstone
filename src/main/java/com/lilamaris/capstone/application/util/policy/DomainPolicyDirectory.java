package com.lilamaris.capstone.application.util.policy;

import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;

public interface DomainPolicyDirectory {
    DomainPolicy<?> policyOf(DomainType domainType);
}
