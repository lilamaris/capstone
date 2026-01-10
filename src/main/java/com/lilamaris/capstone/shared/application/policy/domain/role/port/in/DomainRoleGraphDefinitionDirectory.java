package com.lilamaris.capstone.shared.application.policy.domain.role.port.in;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainRoleGraphDefinitionDirectory {
    DomainRoleGraphDefinition<?> definitionOf(DomainType type);
}
