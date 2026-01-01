package com.lilamaris.capstone.shared.domain.id;

import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface DomainRef {
    DomainType type();

    ExternalizableId id();

    default boolean sameIdentity(DomainRef other) {
        return type() == other.type()
                && id().asString().equals(other.id().asString());
    }
}
