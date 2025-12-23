package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapshotDeltaId extends JpaDefaultUuidDomainId {
    public SnapshotDeltaId(UUID value) {
        super(value);
    }

    public static SnapshotDeltaId newId() {
        return new SnapshotDeltaId(UUID.randomUUID());
    }
}
