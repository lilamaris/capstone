package com.lilamaris.capstone.timeline.domain;

import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.embed.Effective;
import com.lilamaris.capstone.timeline.domain.embed.EffectiveSelector;
import com.lilamaris.capstone.timeline.domain.event.SnapshotSlotCreated;
import com.lilamaris.capstone.timeline.domain.event.SnapshotSlotEffectiveUpdated;
import com.lilamaris.capstone.timeline.domain.event.SnapshotSlotOccupied;
import com.lilamaris.capstone.timeline.domain.event.SnapshotSlotUnoccupied;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "timeline_snapshot_slot")
@EntityListeners(AuditingEntityListener.class)
public class SnapshotSlot implements Identifiable<SnapshotSlotId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SnapshotSlotId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "timeline_id", insertable = false, nullable = false, updatable = false))
    private TimelineId timelineId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "parent_slot_id"))
    private SnapshotSlotId parentSlotId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "snapshot_id"))
    private SnapshotId snapshotId;

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

    protected SnapshotSlot(
            SnapshotSlotId id,
            TimelineId timelineId,
            SnapshotSlotId parentSlotId,
            SnapshotId snapshotId,
            Effective tx,
            Effective valid
    ) {
        this.id = requireField(id, "id");
        this.timelineId = requireField(timelineId, "timelineId");
        this.parentSlotId = parentSlotId;
        this.snapshotId = snapshotId;
        this.tx = requireField(tx, "tx");
        this.valid = requireField(valid, "valid");
    }

    protected static SnapshotSlot create(
            SnapshotSlotId id,
            TimelineId timelineId,
            SnapshotSlotId parentSlotId,
            Effective tx,
            Effective valid
    ) {
        var snapshotSlot = new SnapshotSlot(id, timelineId, parentSlotId, null, tx, valid);
        snapshotSlot.registerCreated();
        return snapshotSlot;
    }

    protected static SnapshotSlot create(
            SnapshotSlotId id,
            TimelineId timelineId,
            Effective tx,
            Effective valid
    ) {
        var snapshotSlot = new SnapshotSlot(id, timelineId, null, null, tx, valid);
        snapshotSlot.registerCreated();
        return snapshotSlot;
    }

    protected static Comparator<SnapshotSlot> sortAsc(EffectiveSelector selector) {
        if (selector == EffectiveSelector.TX) {
            return Comparator.comparing(s -> s.getTx().getFrom());
        } else if (selector == EffectiveSelector.VALID) {
            return Comparator.comparing(s -> s.getValid().getFrom());
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
    }

    protected static Predicate<SnapshotSlot> ifEffectiveOpenEqualTo(EffectiveSelector selector, boolean state) {
        if (selector == EffectiveSelector.TX) {
            return s -> s.getTx().isOpen() == state;
        } else if (selector == EffectiveSelector.VALID) {
            return s -> s.getValid().isOpen() == state;
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
    }

    protected static Predicate<SnapshotSlot> ifEffectiveContains(EffectiveSelector selector, Instant at) {
        if (selector == EffectiveSelector.TX) {
            return s -> s.getTx().contains(at);
        } else if (selector == EffectiveSelector.VALID) {
            return s -> s.getValid().contains(at);
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
    }

    protected static Predicate<SnapshotSlot> ifEffectiveOverlap(EffectiveSelector selector, Effective range) {
        if (selector == EffectiveSelector.TX) {
            return s -> s.getTx().isOverlap(range);
        } else if (selector == EffectiveSelector.VALID) {
            return s -> s.getValid().isOverlap(range);
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
    }

    @Override
    public SnapshotSlotId id() {
        return id;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }

    protected boolean isRoot() {
        return this.parentSlotId == null;
    }

    protected boolean isOccupied() {
        return this.snapshotId != null;
    }

    protected void open(EffectiveSelector selector, Instant at) {
        if (selector == EffectiveSelector.TX) {
            tx.open(at);
        } else if (selector == EffectiveSelector.VALID) {
            valid.open(at);
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
        registerEffectiveUpdated();
    }

    protected void close(EffectiveSelector selector, Instant at) {
        if (selector == EffectiveSelector.TX) {
            tx.close(at);
        } else if (selector == EffectiveSelector.VALID) {
            valid.close(at);
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
        registerEffectiveUpdated();
    }

    protected void attachSnapshot(SnapshotId snapshotId) {
        this.snapshotId = snapshotId;
        registerOccupied();
    }

    protected void detachSnapshot() {
        this.snapshotId = null;
        registerUnoccupied();
    }

    protected List<DomainEvent> pullEvent() {
        var copy = List.copyOf(eventList);
        eventList.clear();
        return copy;
    }

    private void registerCreated() {
        var event = new SnapshotSlotCreated(id, Instant.now());
        eventList.add(event);
    }

    private void registerOccupied() {
        var event = new SnapshotSlotOccupied(id, snapshotId, Instant.now());
        eventList.add(event);
    }

    private void registerUnoccupied() {
        var event = new SnapshotSlotUnoccupied(id, Instant.now());
        eventList.add(event);
    }

    private void registerEffectiveUpdated() {
        var event = new SnapshotSlotEffectiveUpdated(id, tx, valid, Instant.now());
        eventList.add(event);
    }
}
