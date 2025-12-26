package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.model.capstone.timeline.event.SnapshotDeltaCreated;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotDeltaId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.common.embed.impl.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.event.DomainEvent;
import com.lilamaris.capstone.domain.model.common.id.impl.JpaDomainRef;
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
@Table(name = "timeline_snapshot_delta")
@EntityListeners(AuditingEntityListener.class)
public class SnapshotDelta implements Identifiable<SnapshotDeltaId> {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SnapshotDeltaId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "snapshot_link_id", nullable = false, updatable = false))
    private SnapshotLinkId snapshotLinkId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "resource_type")),
            @AttributeOverride(name = "id", column = @Column(name = "resource_id"))
    })
    private JpaDomainRef resourceRef;

    @Embedded
    private JpaAuditMetadata audit;

    @Transient
    private List<DomainEvent> eventList;

    private String jsonPatch;

    protected SnapshotDelta(SnapshotDeltaId id, SnapshotLinkId snapshotLinkId, JpaDomainRef resourceRef, String jsonPatch) {
        this.id = requireField(id, "id");
        this.snapshotLinkId = requireField(snapshotLinkId, "snapshotLinkId");
        this.resourceRef = requireField(resourceRef, "resourceRef");
        this.jsonPatch = requireField(jsonPatch, "jsonPatch");
        this.eventList = new ArrayList<>();
    }

    protected static SnapshotDelta create(SnapshotDeltaId id, SnapshotLinkId snapshotLinkId, JpaDomainRef resourceRef, String jsonPatch) {
        var delta = new SnapshotDelta(id, snapshotLinkId, resourceRef, jsonPatch);
        delta.registerCreated();
        return delta;
    }

    private void registerCreated() {
        var event = new SnapshotDeltaCreated(id, resourceRef.toPOJO(), Instant.now());
        eventList.add(event);
    }


    @Override
    public final SnapshotDeltaId id() {
        return id;
    }
}
