package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultAuditableDomain;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotDeltaId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name = "timeline_snapshot_delta")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapshotDelta extends JpaDefaultAuditableDomain implements Identifiable<SnapshotDeltaId> {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    private SnapshotDeltaId id;

    @Override
    public final SnapshotDeltaId id() {
        return id;
    }
}
