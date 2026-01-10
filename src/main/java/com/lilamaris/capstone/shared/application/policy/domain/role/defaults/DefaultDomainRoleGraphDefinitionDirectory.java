package com.lilamaris.capstone.shared.application.policy.domain.role.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinition;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleGraphDefinitionDirectory;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultDomainRoleGraphDefinitionDirectory implements DomainRoleGraphDefinitionDirectory {
    private final Map<DomainType, DomainRoleGraphDefinition<? extends DomainRole>> definitions = new HashMap<>();

    public void addDefinition(DomainRoleGraphDefinition<?> definition) {
        definitions.put(definition.support(), definition);
    }

    @Override
    public DomainRoleGraphDefinition<?> definitionOf(DomainType type) {
        return Optional.ofNullable(definitions.get(type))
                .orElseThrow(() -> new UnsupportedOperationException("Unknown domain type: " + type));
    }
}
