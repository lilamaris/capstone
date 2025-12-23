package com.lilamaris.capstone.domain.model.common;

public abstract class AbstractDomainId<ID> implements DomainId<ID> {
    @Override
    public boolean equals(Object o) {
        return o != null && this.getClass() == o.getClass() && value().equals(((AbstractDomainId<?>) o).value());
    }

    @Override
    public int hashCode() {
        return value().hashCode();
    }

    @Override
    public final String toString() {
        return value().toString();
    }
}
