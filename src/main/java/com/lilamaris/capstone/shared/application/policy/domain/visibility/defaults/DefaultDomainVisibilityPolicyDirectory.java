package com.lilamaris.capstone.shared.application.policy.domain.visibility.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.visibility.port.in.DomainVisibilityPolicy;
import com.lilamaris.capstone.shared.application.policy.domain.visibility.port.in.DomainVisibilityPolicyDirectory;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultDomainVisibilityPolicyDirectory implements DomainVisibilityPolicyDirectory {
    private final Map<DomainType, DomainVisibilityPolicy> policyMap;

    public DefaultDomainVisibilityPolicyDirectory(List<DomainVisibilityPolicy> policies) {
        this.policyMap = policies.stream().collect(Collectors.toUnmodifiableMap(
                DomainVisibilityPolicy::support,
                Function.identity()
        ));
    }

    @Override
    public DomainVisibilityPolicy policyOf(DomainType type) {
        return Optional.ofNullable(policyMap.get(type))
                .orElseThrow(() -> new UnsupportedOperationException("Unknown domain type " + type));
    }
}
