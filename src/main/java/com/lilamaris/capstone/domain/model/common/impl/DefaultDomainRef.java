package com.lilamaris.capstone.domain.model.common.impl;

import com.lilamaris.capstone.domain.model.common.*;
import java.util.Objects;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

public class DefaultDomainRef implements DomainRef {
    private final DomainType type;
    private final String id;

    public DefaultDomainRef(DomainType type, String id) {
        this.type = requireField(type, "type");
        this.id = requireField(id, "id");
    }

    public static DefaultDomainRef from(DomainType type, DomainId<?> id) {
        return new DefaultDomainRef(type, id.toString());
    }

    @Override
    public DomainType type() {
        return type;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainRef other)) return false;
        return type.equals(other.type()) && id.equals(other.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    @Override
    public String toString() {
        return type + ":" + id;
    }
}
