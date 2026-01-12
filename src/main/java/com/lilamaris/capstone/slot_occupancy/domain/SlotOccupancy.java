package com.lilamaris.capstone.slot_occupancy.domain;


import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.slot_occupancy.domain.event.SlotOccupancyCreated;
import com.lilamaris.capstone.slot_occupancy.domain.event.SlotOccupancyRemoved;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "slot_occupancy")
@EntityListeners(AuditingEntityListener.class)
public class SlotOccupancy implements Persistable<SlotOccupancyId>, Identifiable<SlotOccupancyId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SlotOccupancyId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "snapshot_id", nullable = false, updatable = false))
    private SnapshotId snapshotId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "snapshot_slot_id", nullable = false, updatable = false))
    private SnapshotSlotId snapshotSlotId;
    @Transient
    private boolean isNew = true;

    public SlotOccupancy(
            SlotOccupancyId id,
            SnapshotSlotId snapshotSlotId,
            SnapshotId snapshotId
    ) {
        this.id = requireField(id, "id");
        this.snapshotSlotId = requireField(snapshotSlotId, "snapshotSlotId");
        this.snapshotId = requireField(snapshotId, "snapshotId");
    }

    public static SlotOccupancy create(Supplier<SlotOccupancyId> idSupplier, SnapshotSlotId snapshotSlotId, SnapshotId snapshotId) {
        var id = idSupplier.get();
        var slotOccupancy = new SlotOccupancy(id, snapshotSlotId, snapshotId);
        slotOccupancy.registerCreated();
        return slotOccupancy;
    }

    @Override
    public SlotOccupancyId id() {
        return id;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }

    @PostRemove
    private void registerRemoved() {
        var event = new SlotOccupancyRemoved(id, Instant.now());
        eventList.add(event);
    }

    private void registerCreated() {
        var event = new SlotOccupancyCreated(id, Instant.now());
        eventList.add(event);
    }

    @Override
    public SlotOccupancyId getId() {
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
