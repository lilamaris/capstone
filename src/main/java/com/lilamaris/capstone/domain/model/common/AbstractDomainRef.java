package com.lilamaris.capstone.domain.model.common;

public abstract class AbstractDomainRef implements DomainRef {
    protected abstract Object identity();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDomainRef that = (AbstractDomainRef) o;
        return identity().equals(that.identity());
    }

    @Override
    public int hashCode() {
        return identity().hashCode();
    }
}
