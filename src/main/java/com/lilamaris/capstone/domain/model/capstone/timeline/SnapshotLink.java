package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.model.capstone.timeline.event.SnapshotLinked;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.embed.impl.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.event.DomainEvent;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "timeline_snapshot_link")
@EntityListeners(AuditingEntityListener.class)
public class SnapshotLink implements Identifiable<SnapshotLinkId> {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();
    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();
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

    protected SnapshotLink(
            SnapshotLinkId id,
            TimelineId timelineId,
            SnapshotId ancestorSnapshotId,
            SnapshotId descendantSnapshotId
    ) {
        this.id = requireField(id, "id");
        this.timelineId = requireField(timelineId, "timelineId");
        this.descendantSnapshotId = requireField(descendantSnapshotId, "descendantSnapshotId");
        this.ancestorSnapshotId = ancestorSnapshotId;
    }

    protected static SnapshotLink create(
            SnapshotLinkId id,
            TimelineId timelineId,
            SnapshotId ancestorSnapshotId,
            SnapshotId descendantSnapshotId
    ) {
        var snapshotLink = new SnapshotLink(id, timelineId, ancestorSnapshotId, descendantSnapshotId);
        snapshotLink.registerCreated();
        return snapshotLink;
    }

    private void registerCreated() {
        var event = new SnapshotLinked(id, ancestorSnapshotId, descendantSnapshotId, Instant.now());
        eventList.add(event);
    }

    protected List<DomainEvent> pullEvent() {
        var copy = List.copyOf(eventList);
        eventList.clear();
        return copy;
    }

    public boolean isRoot() {
        return ancestorSnapshotId == null;
    }

    @Override
    public final SnapshotLinkId id() {
        return id;
    }
}
