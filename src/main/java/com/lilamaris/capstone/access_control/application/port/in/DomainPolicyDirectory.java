package com.lilamaris.capstone.access_control.application.port.in;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainPolicyDirectory {
    DomainPolicy<?> policyOf(DomainType domainType);
}
