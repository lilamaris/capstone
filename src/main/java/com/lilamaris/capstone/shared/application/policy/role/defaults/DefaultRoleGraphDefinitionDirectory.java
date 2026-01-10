package com.lilamaris.capstone.shared.application.policy.role.defaults;

import com.lilamaris.capstone.shared.application.policy.role.port.in.ResourceRole;
import com.lilamaris.capstone.shared.application.policy.role.port.in.RoleGraphDefinition;
import com.lilamaris.capstone.shared.application.policy.role.port.in.RoleGraphDefinitionDirectory;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultRoleGraphDefinitionDirectory implements RoleGraphDefinitionDirectory {
    private final Map<DomainType, RoleGraphDefinition<? extends ResourceRole>> definitions = new HashMap<>();

    public void addDefinition(RoleGraphDefinition<?> definition) {
        definitions.put(definition.support(), definition);
    }

    @Override
    public RoleGraphDefinition<?> definitionOf(DomainType type) {
        return Optional.ofNullable(definitions.get(type))
                .orElseThrow(() -> new UnsupportedOperationException("Unknown domain type: " + type));
    }
}
