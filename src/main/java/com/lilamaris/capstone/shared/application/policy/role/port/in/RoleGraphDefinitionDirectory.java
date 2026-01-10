package com.lilamaris.capstone.shared.application.policy.role.port.in;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface RoleGraphDefinitionDirectory {
    RoleGraphDefinition<?> definitionOf(DomainType type);
}
