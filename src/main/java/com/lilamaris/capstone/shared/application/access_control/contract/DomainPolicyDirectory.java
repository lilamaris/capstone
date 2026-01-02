package com.lilamaris.capstone.shared.application.access_control.contract;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainPolicyDirectory {
    DomainPolicy<?> policyOf(DomainType domainType);
}
