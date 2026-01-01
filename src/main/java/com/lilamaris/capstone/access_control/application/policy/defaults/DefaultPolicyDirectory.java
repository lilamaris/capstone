package com.lilamaris.capstone.access_control.application.policy.defaults;

import com.lilamaris.capstone.access_control.application.port.in.DomainPolicy;
import com.lilamaris.capstone.access_control.application.port.in.DomainPolicyDirectory;
import com.lilamaris.capstone.access_control.application.port.in.DomainRole;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultPolicyDirectory implements DomainPolicyDirectory {
    private final Map<DomainType, DomainPolicy<? extends DomainRole>> policies = new HashMap<>();

    public void addPolicy(DomainType type, DomainPolicy<?> policy) {
        policies.put(type, policy);
    }

    @Override
    public DomainPolicy<? extends DomainRole> policyOf(DomainType type) {
        return Optional.ofNullable(policies.get(type))
                .orElseThrow(() -> new UnsupportedOperationException("Unknown domain type" + type));
    }
}
