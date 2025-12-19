package com.lilamaris.capstone.domain;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractUUIDDomainId<D extends DomainType> implements DomainId<UUID, D> {
    private final UUID value;

    public AbstractUUIDDomainId(UUID value) {
        this.value = value;
    }

    public AbstractUUIDDomainId() {
        this.value = UUID.randomUUID();
    }

    @Override
    public UUID getValue() {
        return value;
    }

    @Override
    public String asString() {
        return value.toString();
    }

    @Override
    public abstract D getDomainType();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractUUIDDomainId<?> other)) return false;
        return getValue().equals(other.getValue()) && getDomainType().equals(other.getDomainType());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getValue(), getDomainType());
    }

    @Override
    public String toString() {
        return getDomainType().getName() + ":" + value;
    }
}
