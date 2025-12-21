package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.lilamaris.capstone.domain.common.impl.DefaultUuidDomainId;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class SnapshotId extends DefaultUuidDomainId {
    public SnapshotId() {
        super();
    }

    public SnapshotId(UUID value) {
        super(value);
    }

    public static SnapshotId newId() {
        return new SnapshotId();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
