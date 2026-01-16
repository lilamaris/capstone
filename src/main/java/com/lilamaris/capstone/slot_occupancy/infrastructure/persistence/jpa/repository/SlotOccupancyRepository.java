package com.lilamaris.capstone.slot_occupancy.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.slot_occupancy.domain.SlotOccupancy;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SlotOccupancyRepository extends JpaRepository<SlotOccupancy, SlotOccupancyId> {
    boolean existsBySlotIdOrSnapshotId(SlotId slotId, SnapshotId snapshotId);

    Optional<SlotOccupancy> findBySnapshotId(SnapshotId snapshotId);

    List<SlotOccupancy> findAllBySnapshotIdIn(Iterable<SnapshotId> snapshotIds);

    Optional<SlotOccupancy> findBySlotId(SlotId slotId);

    List<SlotOccupancy> findAllBySlotIdIn(Iterable<SlotId> slotIds);
}
