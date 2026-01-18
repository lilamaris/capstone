package com.lilamaris.capstone.shared.application.support.defaults;

import com.lilamaris.capstone.shared.application.support.Definition;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultDomainTypeBasedDefinition<V> implements Definition<DomainType, V> {
    private final DomainType type;

    @Override
    public DomainType support() {
        return type;
    }
}
