package com.lilamaris.capstone.domain.model.common.impl.jpa;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.domain.model.common.AbstractDomainId;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaDefaultUuidDomainId extends AbstractDomainId<UUID> {
    @JsonValue
    @Column(name = "id", nullable = false, updatable = false)
    protected UUID id;

    @Override
    public UUID value() {
        return id;
    }

    protected JpaDefaultUuidDomainId(UUID value) {
        id = value;
    }
}
