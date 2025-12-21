package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.lilamaris.capstone.domain.common.impl.DefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.ToString;

import java.util.UUID;

@Embeddable
public class SnapshotDeltaId extends DefaultUuidDomainId {
    public SnapshotDeltaId() {
        super();
    }

    public SnapshotDeltaId(UUID value) {
        super(value);
    }

    public static SnapshotDeltaId newId() {
        return new SnapshotDeltaId();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
