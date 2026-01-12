package com.lilamaris.capstone.slot_occupancy.application.port.out;

import com.lilamaris.capstone.slot_occupancy.domain.SlotOccupancy;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;

import java.util.List;
import java.util.Optional;

public interface SlotOccupancyPort {
    boolean existsBySlotIdOrSnapshotId(SnapshotSlotId snapshotSlotId, SnapshotId snapshotId);

    Optional<SlotOccupancy> getById(SlotOccupancyId id);

    List<SlotOccupancy> getByIds(List<SlotOccupancyId> ids);

    Optional<SlotOccupancy> getBySlotId(SnapshotSlotId slotId);

    List<SlotOccupancy> getBySlotIds(List<SnapshotSlotId> slotIds);

    Optional<SlotOccupancy> getBySnapshotId(SnapshotId snapshotId);

    List<SlotOccupancy> getBySnapshotIds(List<SnapshotId> snapshotIds);

    SlotOccupancy save(SlotOccupancy slotOccupancy);

    void deleteById(SlotOccupancyId id);
}
