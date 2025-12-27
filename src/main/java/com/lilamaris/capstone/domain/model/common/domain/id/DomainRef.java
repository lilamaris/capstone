package com.lilamaris.capstone.domain.model.common.domain.id;

import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;

public interface DomainRef {
    DomainType type();

    ExternalizableId id();

    default boolean sameIdentity(DomainRef other) {
        return type() == other.type()
                && id().asString().equals(other.id().asString());
    }
}
