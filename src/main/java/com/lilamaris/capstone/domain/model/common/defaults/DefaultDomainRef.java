package com.lilamaris.capstone.domain.model.common.defaults;

import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;

import java.util.Objects;

public record DefaultDomainRef(DomainType type, ExternalizableId id) implements DomainRef {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainRef other)) return false;
        return type.equals(other.type())
                && id().asString().equals(other.id().asString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id().asString());
    }
}
