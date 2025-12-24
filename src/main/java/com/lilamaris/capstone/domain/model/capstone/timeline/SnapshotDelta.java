package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotDeltaId;
import com.lilamaris.capstone.domain.model.common.embed.impl.JpaDefaultAuditableDomain;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import jakarta.persistence.*;
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
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SnapshotDeltaId id;

    @Override
    public final SnapshotDeltaId id() {
        return id;
    }
}
