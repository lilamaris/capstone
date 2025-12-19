package com.lilamaris.capstone.domain.embed;

import com.lilamaris.capstone.domain.DomainId;

public record DomainRef(
        String id,
        String type
) {
    public static DomainRef from(DomainId<?, ?> domainId) {
        var id = domainId.asString();
        var type = domainId.getDomainType().getName();
        return new DomainRef(id, type);
    }

    public boolean matches(DomainId<?, ?> domainId) {
        var thatId = domainId.asString();
        var thatType = domainId.getDomainType().getName();
        return type.equals(thatType) && id.equals(thatId);
    }
}
