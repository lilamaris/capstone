package com.lilamaris.capstone.shared.application.policy.domain.visibility.port.in;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainVisibilityPolicyDirectory {
    DomainVisibilityPolicy policyOf(DomainType type);
}
