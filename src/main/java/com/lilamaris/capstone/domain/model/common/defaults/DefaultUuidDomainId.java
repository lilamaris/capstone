package com.lilamaris.capstone.domain.model.common.defaults;

import com.lilamaris.capstone.domain.model.common.id.AbstractDomainId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DefaultUuidDomainId extends AbstractDomainId<UUID> {
    protected DefaultUuidDomainId(UUID value) {
        init(value);
    }

    protected static UUID newUuid() {
        return UUID.randomUUID();
    }

    protected abstract void init(UUID value);
}
