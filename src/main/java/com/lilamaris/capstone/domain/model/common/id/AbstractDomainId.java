package com.lilamaris.capstone.domain.model.common.id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractDomainId<ID> implements DomainId<ID> {
    @Override
    public final boolean equals(Object o) {
        return o != null && this.getClass() == o.getClass() && value().equals(((AbstractDomainId<?>) o).value());
    }

    @Override
    public final int hashCode() {
        return value().hashCode();
    }

    @Override
    public final String toString() {
        return value().toString();
    }
}
