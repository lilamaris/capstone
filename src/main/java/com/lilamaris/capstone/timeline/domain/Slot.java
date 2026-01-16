package com.lilamaris.capstone.timeline.domain;

import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.timeline.domain.embed.Effective;
import com.lilamaris.capstone.timeline.domain.embed.EffectiveSelector;
import com.lilamaris.capstone.timeline.domain.event.SlotCreated;
import com.lilamaris.capstone.timeline.domain.event.SlotEffectiveUpdated;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "timeline_slot")
@EntityListeners(AuditingEntityListener.class)
public class Slot implements Persistable<SlotId>, Identifiable<SlotId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SlotId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "timeline_id", insertable = false, nullable = false, updatable = false))
    private TimelineId timelineId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "parent_slot_id"))
    private SlotId parentSlotId;

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

    protected Slot(
            SlotId id,
            TimelineId timelineId,
            SlotId parentSlotId,
            Effective tx,
            Effective valid
    ) {
        this.id = requireField(id, "id");
        this.timelineId = requireField(timelineId, "timelineId");
        this.parentSlotId = parentSlotId;
        this.tx = requireField(tx, "tx");
        this.valid = requireField(valid, "valid");
    }

    protected static Slot create(
            Supplier<SlotId> idSupplier,
            TimelineId timelineId,
            SlotId parentSlotId,
            Effective tx,
            Effective valid
    ) {
        var id = idSupplier.get();
        var snapshotSlot = new Slot(id, timelineId, parentSlotId, tx, valid);
        snapshotSlot.registerCreated();
        return snapshotSlot;
    }

    protected static Slot create(
            Supplier<SlotId> idSupplier,
            TimelineId timelineId,
            Effective tx,
            Effective valid
    ) {
        var id = idSupplier.get();
        var snapshotSlot = new Slot(id, timelineId, null, tx, valid);
        snapshotSlot.registerCreated();
        return snapshotSlot;
    }

    protected static Comparator<Slot> sortAsc(EffectiveSelector selector) {
        if (selector == EffectiveSelector.TX) {
            return Comparator.comparing(s -> s.getTx().getFrom());
        } else if (selector == EffectiveSelector.VALID) {
            return Comparator.comparing(s -> s.getValid().getFrom());
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
    }

    protected static Predicate<Slot> ifEffectiveOpenEqualTo(EffectiveSelector selector, boolean state) {
        if (selector == EffectiveSelector.TX) {
            return s -> s.getTx().isOpen() == state;
        } else if (selector == EffectiveSelector.VALID) {
            return s -> s.getValid().isOpen() == state;
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
    }

    protected static Predicate<Slot> ifEffectiveContains(EffectiveSelector selector, Instant at) {
        if (selector == EffectiveSelector.TX) {
            return s -> s.getTx().contains(at);
        } else if (selector == EffectiveSelector.VALID) {
            return s -> s.getValid().contains(at);
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
    }

    protected static Predicate<Slot> ifEffectiveOverlap(EffectiveSelector selector, Effective range) {
        if (selector == EffectiveSelector.TX) {
            return s -> s.getTx().isOverlap(range);
        } else if (selector == EffectiveSelector.VALID) {
            return s -> s.getValid().isOverlap(range);
        } else {
            throw new DomainIllegalArgumentException("Unsupported selector: " + selector);
        }
    }

    @Override
    public SlotId id() {
        return id;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }

    public boolean isRoot() {
        return this.parentSlotId == null;
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

    protected List<DomainEvent> pullEvent() {
        var copy = List.copyOf(eventList);
        eventList.clear();
        return copy;
    }

    private void registerCreated() {
        var event = new SlotCreated(id, Instant.now());
        eventList.add(event);
    }

    private void registerEffectiveUpdated() {
        var event = new SlotEffectiveUpdated(id, tx, valid, Instant.now());
        eventList.add(event);
    }

    @Transient
    private boolean isNew = true;

    @Override
    public SlotId getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    private void markNotNew() {
        this.isNew = false;
    }
}
