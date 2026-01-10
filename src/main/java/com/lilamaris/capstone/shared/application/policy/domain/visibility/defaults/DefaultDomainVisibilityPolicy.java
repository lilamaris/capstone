package com.lilamaris.capstone.shared.application.policy.domain.visibility.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.visibility.port.in.DomainVisibilityPolicy;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class DefaultDomainVisibilityPolicy implements DomainVisibilityPolicy {
    private final DomainType type;

    @Setter private DomainRole discoverableRole;
    @Setter private DomainRole readableRole;

    @Override
    public DomainType support() {
        return type;
    }

    @Override
    public DomainRole discoverableRole() {
        return discoverableRole;
    }

    @Override
    public DomainRole readableRole() {
        return readableRole;
    }
}
