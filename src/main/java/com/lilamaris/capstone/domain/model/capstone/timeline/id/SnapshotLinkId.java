package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.lilamaris.capstone.domain.common.impl.DefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.ToString;

import java.util.UUID;

@ToString(callSuper = true)
@Embeddable
public class SnapshotLinkId extends DefaultUuidDomainId {
    public SnapshotLinkId() {
        super();
    }

    public SnapshotLinkId(UUID value) {
        super(value);
    }

    public static SnapshotLinkId newId() {
        return new SnapshotLinkId();
    }
}
