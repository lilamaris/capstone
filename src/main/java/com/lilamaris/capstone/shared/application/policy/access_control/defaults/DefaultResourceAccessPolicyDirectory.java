package com.lilamaris.capstone.shared.application.policy.access_control.defaults;

import com.lilamaris.capstone.shared.application.policy.access_control.port.in.ResourceAccessPolicy;
import com.lilamaris.capstone.shared.application.policy.access_control.port.in.ResourceAccessPolicyDirectory;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultResourceAccessPolicyDirectory implements ResourceAccessPolicyDirectory {
    private final Map<DomainType, ResourceAccessPolicy> policies = new HashMap<>();

    public void addPolicy(ResourceAccessPolicy policy) {
        policies.put(policy.support(), policy);
    }

    @Override
    public ResourceAccessPolicy policyOf(DomainType type) {
        return Optional.ofNullable(policies.get(type))
                .orElseThrow(() -> new UnsupportedOperationException("Unknown domain type" + type));
    }
}
