package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.domain.model.common.impl.DefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapshotId extends DefaultUuidDomainId {
    @JsonValue
    protected UUID value;

    public static SnapshotId newId() {
        return new SnapshotId(newUuid());
    }

    @Override
    public UUID value() {
        return value;
    }

    @Override
    protected void init(UUID value) {
        this.value = value;
    }

    public SnapshotId(UUID value) {
        super(value);
    }
}
