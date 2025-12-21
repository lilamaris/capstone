package com.lilamaris.capstone.domain.common;

import java.util.Objects;

public abstract class AbstractDomainRef implements DomainRef {
    private final DomainType type;
    private final DomainId<?> id;

    public AbstractDomainRef(DomainType type, DomainId<?> id) {
        this.type = Objects.requireNonNull(type);
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDomainRef that = (AbstractDomainRef) o;
        return type.equals(that.type) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
