package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.event.SnapshotCreated;
import com.lilamaris.capstone.domain.model.capstone.timeline.event.SnapshotEffectiveChanged;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.embed.impl.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.embed.impl.jpa.JpaDescriptionMetadata;
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
@Table(name = "timeline_snapshot")
@EntityListeners(AuditingEntityListener.class)
public class Snapshot implements Identifiable<SnapshotId> {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();
    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SnapshotId id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "tx_from")),
            @AttributeOverride(name = "to", column = @Column(name = "tx_to")),
    })
    private Effective tx;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "valid_from")),
            @AttributeOverride(name = "to", column = @Column(name = "valid_to")),
    })
    private Effective valid;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "timeline_id", insertable = false, updatable = false))
    private TimelineId timelineId;
    @Embedded
    private JpaDescriptionMetadata descriptionMetadata;
    private Integer versionNo;

    protected Snapshot(
            SnapshotId id,
            Effective tx,
            Effective valid,
            TimelineId timelineId,
            Integer versionNo,
            JpaDescriptionMetadata descriptionMetadata
    ) {
        this.id = requireField(id, "id");
        this.tx = requireField(tx, "tx");
        this.valid = requireField(valid, "valid");
        this.timelineId = requireField(timelineId, "timelineId");
        this.versionNo = requireField(versionNo, "versionNo");
        this.descriptionMetadata = requireField(descriptionMetadata, "descriptionMetadata");
    }

    protected static Snapshot create(
            SnapshotId id,
            Effective tx,
            Effective valid,
            TimelineId timelineId,
            Integer versionNo,
            String title
    ) {
        var descriptionMetadata = JpaDescriptionMetadata.create(title);
        var snapshot = new Snapshot(id, tx, valid, timelineId, versionNo, descriptionMetadata);
        snapshot.registerCreated();
        return snapshot;
    }

    @Override
    public final SnapshotId id() {
        return id;
    }

    protected void open(EffectiveSelector selector, Instant at) {
        if (selector == EffectiveSelector.TX) {
            tx.open(at);
        } else {
            valid.open(at);
        }
        upgrade();
        registerEffectiveChanged(selector, at);
    }

    protected void close(EffectiveSelector selector, Instant at) {
        if (selector == EffectiveSelector.TX) {
            tx.close(at);
        } else {
            valid.close(at);
        }
        upgrade();
        registerEffectiveChanged(selector, at);
    }

    protected List<DomainEvent> pullEvent() {
        var copy = List.copyOf(eventList);
        eventList.clear();
        return copy;
    }

    private void registerCreated() {
        var event = new SnapshotCreated(id, Instant.now());
        eventList.add(event);
    }

    private void registerEffectiveChanged(EffectiveSelector selector, Instant at) {
        var event = new SnapshotEffectiveChanged(id, selector, at, Instant.now());
        eventList.add(event);
    }

    private void upgrade() {
        versionNo += 1;
    }
}
