package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapshotId extends JpaDefaultUuidDomainId {
    public SnapshotId(UUID value) {
        super(value);
    }

    public static SnapshotId newId() {
        return new SnapshotId(UUID.randomUUID());
    }
}
