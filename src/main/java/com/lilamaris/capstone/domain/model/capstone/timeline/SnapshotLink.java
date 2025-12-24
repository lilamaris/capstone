package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@Entity
@Table(name = "timeline_snapshot_link")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapshotLink implements Identifiable<SnapshotLinkId> {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SnapshotLinkId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "timeline_id", insertable = false, updatable = false))
    private TimelineId timelineId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "ancestor_snapshot_id"))
    private SnapshotId ancestorSnapshotId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "descendant_snapshot_id"))
    private SnapshotId descendantSnapshotId;

    protected SnapshotLink(SnapshotLinkId id, TimelineId timelineId, SnapshotId ancestorSnapshotId, SnapshotId descendantSnapshotId) {
        this.id = requireField(id, "id");
        this.timelineId = requireField(timelineId, "timelineId");
        this.descendantSnapshotId = requireField(descendantSnapshotId, "descendantSnapshotId");
        this.ancestorSnapshotId = ancestorSnapshotId;
    }

    public boolean isRoot() {
        return ancestorSnapshotId == null;
    }

    @Override
    public final SnapshotLinkId id() {
        return id;
    }
}
