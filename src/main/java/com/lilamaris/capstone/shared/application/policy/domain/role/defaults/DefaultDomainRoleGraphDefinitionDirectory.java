package com.lilamaris.capstone.shared.application.policy.domain.role.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinition;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinitionDirectory;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultDomainRoleGraphDefinitionDirectory implements DomainRoleGraphDefinitionDirectory {
    private final Map<DomainType, DomainRoleGraphDefinition<? extends DomainRole>> definitions;

    public DefaultDomainRoleGraphDefinitionDirectory(List<DomainRoleGraphDefinition<?>> definitions) {
        this.definitions = definitions.stream()
                .collect(Collectors.toUnmodifiableMap(
                        DomainRoleGraphDefinition::support,
                        Function.identity()
                ));
    }

    @Override
    public DomainRoleGraphDefinition<?> definitionOf(DomainType type) {
        return Optional.ofNullable(definitions.get(type))
                .orElseThrow(() -> new UnsupportedOperationException("Unknown domain type: " + type));
    }
}
