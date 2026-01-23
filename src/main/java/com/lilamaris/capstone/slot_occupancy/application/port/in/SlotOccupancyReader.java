package com.lilamaris.capstone.slot_occupancy.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.util.List;

public interface SlotOccupancyReader {
    List<SlotOccupancyEntry> resolveByRefs(List<DomainRef> refs);

    SlotOccupancyEntry resolveByRef(DomainRef ref);

    List<SlotOccupancyEntry> getBySlotRefs(List<DomainRef> refs);

    SlotOccupancyEntry getBySlotRef(DomainRef ref);

    List<SlotOccupancyEntry> getBySnapshotRefs(List<DomainRef> refs);

    SlotOccupancyEntry getBySnapshotRef(DomainRef ref);
}
