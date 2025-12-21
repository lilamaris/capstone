package com.lilamaris.capstone.domain.common;

import lombok.ToString;

@ToString
public abstract class AbstractDomainId<ID> implements DomainId<ID> {
    @Override
    public boolean equals(Object o) {
        return o != null && this.getClass() == o.getClass() && value().equals(((AbstractDomainId<?>) o).value());
    }

    @Override
    public int hashCode() {
        return value().hashCode();
    }
}
