package com.lilamaris.capstone.shared.application.policy.resource.access_control.defaults;

import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAccessPolicy;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAccessPolicyDirectory;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultResourceAccessPolicyDirectory implements ResourceAccessPolicyDirectory {
    private final Map<DomainType, ResourceAccessPolicy> policies;

    public DefaultResourceAccessPolicyDirectory(List<ResourceAccessPolicy> policies) {
        this.policies = policies.stream()
                .collect(Collectors.toUnmodifiableMap(
                        ResourceAccessPolicy::support,
                        Function.identity()
                ));
    }

    @Override
    public ResourceAccessPolicy policyOf(DomainType type) {
        return Optional.ofNullable(policies.get(type))
                .orElseThrow(() -> new UnsupportedOperationException("Unknown domain type" + type));
    }
}
