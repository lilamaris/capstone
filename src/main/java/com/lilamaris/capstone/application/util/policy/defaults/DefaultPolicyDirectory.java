package com.lilamaris.capstone.application.util.policy.defaults;

import com.lilamaris.capstone.application.util.policy.DomainPolicy;
import com.lilamaris.capstone.application.util.policy.DomainPolicyDirectory;
import com.lilamaris.capstone.application.util.policy.DomainRole;
import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;

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
