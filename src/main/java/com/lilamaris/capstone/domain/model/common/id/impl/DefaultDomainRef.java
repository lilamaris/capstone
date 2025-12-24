package com.lilamaris.capstone.domain.model.common.id.impl;

import com.lilamaris.capstone.domain.model.common.id.DomainId;
import com.lilamaris.capstone.domain.model.common.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.type.DomainType;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

public record DefaultDomainRef(DomainType type, String id) implements DomainRef {
    public DefaultDomainRef(DomainType type, String id) {
        this.type = requireField(type, "type");
        this.id = requireField(id, "id");
    }

    public static DefaultDomainRef from(DomainType type, DomainId<?> id) {
        return new DefaultDomainRef(type, id.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainRef other)) return false;
        return type.equals(other.type()) && id.equals(other.id());
    }

    @Override
    public String toString() {
        return type + ":" + id;
    }
}
