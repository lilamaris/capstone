package com.lilamaris.capstone.slot_occupancy.application.service;

import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyEntry;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyReader;
import com.lilamaris.capstone.slot_occupancy.application.port.out.SlotOccupancyStore;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotOccupancyReaderService implements SlotOccupancyReader {
    private final SlotOccupancyStore slotOccupancyStore;
    private final DomainRefResolverDirectory refDir;

    @Override
    public List<SlotOccupancyEntry> resolveByRefs(List<DomainRef> refs) {
        var ids = refDir.resolve(refs, SlotOccupancyId.class);
        return slotOccupancyStore.getByIds(ids).stream().map(SlotOccupancyEntry::from).toList();
    }

    @Override
    public SlotOccupancyEntry resolveByRef(DomainRef ref) {
        var id = refDir.resolve(ref, SlotOccupancyId.class);
        return slotOccupancyStore.getById(id).map(SlotOccupancyEntry::from)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "SlotOccupancy with ref '%s' not found.", id
                )));
    }

    @Override
    public List<SlotOccupancyEntry> getBySlotRefs(List<DomainRef> refs) {
        var ids = refDir.resolve(refs, SlotId.class);
        return slotOccupancyStore.getBySlotIds(ids).stream().map(SlotOccupancyEntry::from).toList();
    }

    @Override
    public List<SlotOccupancyEntry> getBySnapshotRefs(List<DomainRef> refs) {
        var ids = refDir.resolve(refs, SnapshotId.class);
        return slotOccupancyStore.getBySnapshotIds(ids).stream().map(SlotOccupancyEntry::from).toList();
    }
}
