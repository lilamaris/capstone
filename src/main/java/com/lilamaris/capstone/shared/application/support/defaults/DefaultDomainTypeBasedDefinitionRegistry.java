package com.lilamaris.capstone.shared.application.support.defaults;

import com.lilamaris.capstone.shared.application.support.Definition;
import com.lilamaris.capstone.shared.application.support.DefinitionRegistry;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultDomainTypeBasedDefinitionRegistry<V extends Definition<DomainType, ?>>
        implements DefinitionRegistry<DomainType, V> {
    private final Map<DomainType, V> registries;

    public DefaultDomainTypeBasedDefinitionRegistry(
            List<V> registries
    ) {
        this.registries = registries.stream()
                .collect(Collectors.toUnmodifiableMap(
                        Definition::support,
                        Function.identity()
                ));
    }

    @Override
    public V definitionOf(DomainType key) {
        return Optional.ofNullable(registries.get(key))
                .orElseThrow(() -> new UnsupportedOperationException(String.format(
                        "Unknown support key '%s'", key
                )));
    }
}
