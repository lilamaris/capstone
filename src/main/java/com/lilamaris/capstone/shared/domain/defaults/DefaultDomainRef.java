package com.lilamaris.capstone.shared.domain.defaults;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public record DefaultDomainRef(
        DomainType type,
        ExternalizableId id
) implements DomainRef {
    public static DefaultDomainRef from(DomainRef ref) {
        return new DefaultDomainRef(ref.type(), ref.id());
    }

    public static DefaultDomainRef from(DomainType type, ExternalizableId id) {
        return new DefaultDomainRef(type, id);
    }
}
