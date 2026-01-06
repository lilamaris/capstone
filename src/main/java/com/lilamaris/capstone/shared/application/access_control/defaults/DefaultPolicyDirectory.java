package com.lilamaris.capstone.shared.application.access_control.defaults;

import com.lilamaris.capstone.shared.application.access_control.contract.DomainPolicy;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainPolicyDirectory;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainRole;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultPolicyDirectory implements DomainPolicyDirectory {
    private final Map<DomainType, DomainPolicy<? extends DomainRole>> policies = new HashMap<>();

    public void addPolicy(DomainPolicy<?> policy) {
        policies.put(policy.supportType(), policy);
    }

    @Override
    public DomainPolicy<? extends DomainRole> policyOf(DomainType type) {
        return Optional.ofNullable(policies.get(type))
                .orElseThrow(() -> new UnsupportedOperationException("Unknown domain type" + type));
    }
}
