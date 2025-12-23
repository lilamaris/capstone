package com.lilamaris.capstone.domain.model.common.impl;

import com.lilamaris.capstone.domain.model.common.AbstractDomainId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DefaultUuidDomainId extends AbstractDomainId<UUID> {
    protected DefaultUuidDomainId(UUID value) {
        init(value);
    }

    protected abstract void init(UUID value);

    protected static UUID newUuid() {
        return UUID.randomUUID();
    }
}
