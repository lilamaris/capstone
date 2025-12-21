package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.lilamaris.capstone.domain.common.impl.DefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.ToString;

import java.util.UUID;

@ToString(callSuper = true)
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
}
