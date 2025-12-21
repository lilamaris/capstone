package com.lilamaris.capstone.domain.common.impl;

import com.lilamaris.capstone.domain.common.AbstractDomainId;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.ToString;

import java.util.UUID;

@ToString(callSuper = true)
@MappedSuperclass
public class DefaultUuidDomainId extends AbstractDomainId<UUID> {
    @Column(name = "id", nullable = false, updatable = false)
    protected UUID id;

    @Override
    public UUID value() {
        return id;
    }

    protected DefaultUuidDomainId(UUID value) {
        id = value;
    }

    protected DefaultUuidDomainId() {
        id = UUID.randomUUID();
    }
}
