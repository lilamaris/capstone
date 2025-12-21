package com.lilamaris.capstone.domain.common.impl;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.domain.common.AbstractDomainId;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public class DefaultUuidDomainId extends AbstractDomainId<UUID> {
    @JsonValue
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

    @Override
    public String toString() {
        return id.toString();
    }
}
