package com.lilamaris.capstone.domain;

import java.util.Objects;

public abstract class AbstractStringDomainId<D extends DomainType> implements DomainId<String, D> {
    private final String value;

    public AbstractStringDomainId(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public abstract D getDomainType();


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractStringDomainId<?> other)) return false;
        return value.equals(other.value) && getDomainType().equals(other.getDomainType());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value, getDomainType());
    }

    @Override
    public String toString() {
        return getDomainType().getName() + ":" + value;
    }
}
