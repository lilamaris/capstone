package com.lilamaris.capstone.slot_occupancy.application.service;

import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.slot_occupancy.application.exception.AlreadyOccupiedException;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyCreator;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyEntry;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyReader;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyRemover;
import com.lilamaris.capstone.slot_occupancy.application.port.out.SlotOccupancyStore;
import com.lilamaris.capstone.slot_occupancy.domain.SlotOccupancy;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class SlotOccupancyService implements
        SlotOccupancyReader,
        SlotOccupancyCreator,
        SlotOccupancyRemover {
    private final SlotOccupancyStore slotOccupancyStore;
    private final IdGenerationDirectory ids;
    private final DomainRefResolverDirectory refDir;

    private Supplier<ResourceNotFoundException> resourceNotFoundExceptionSupplier(DomainRef ref) {
        return () -> new ResourceNotFoundException(String.format(
                "Slot occupancy not found with reference type '%s' and reference id '%s'",
                ref.type().name(),
                ref.id().asString()
        ));
    }

    @Override
    public List<SlotOccupancyEntry> resolveByRefs(List<DomainRef> refs) {
        var ids = refDir.resolve(refs, SlotOccupancyId.class);
        return slotOccupancyStore.getByIds(ids).stream().map(SlotOccupancyEntry::from).toList();
    }

    @Override
    public SlotOccupancyEntry resolveByRef(DomainRef ref) {
        var id = refDir.resolve(ref, SlotOccupancyId.class);
        return slotOccupancyStore.getById(id)
                .map(SlotOccupancyEntry::from)
                .orElseThrow(resourceNotFoundExceptionSupplier(ref));
    }

    @Override
    public List<SlotOccupancyEntry> getBySlotRefs(List<DomainRef> refs) {
        var ids = refDir.resolve(refs, SlotId.class);
        return slotOccupancyStore.getBySlotIds(ids).stream().map(SlotOccupancyEntry::from).toList();
    }

    @Override
    public SlotOccupancyEntry getBySlotRef(DomainRef ref) {
        var id = refDir.resolve(ref, SlotId.class);
        return slotOccupancyStore.getBySlotId(id)
                .map(SlotOccupancyEntry::from)
                .orElseThrow(resourceNotFoundExceptionSupplier(ref));
    }

    @Override
    public List<SlotOccupancyEntry> getBySnapshotRefs(List<DomainRef> refs) {
        var ids = refDir.resolve(refs, SnapshotId.class);
        return slotOccupancyStore.getBySnapshotIds(ids).stream().map(SlotOccupancyEntry::from).toList();
    }

    @Override
    public SlotOccupancyEntry getBySnapshotRef(DomainRef ref) {
        var id = refDir.resolve(ref, SnapshotId.class);
        return slotOccupancyStore.getBySnapshotId(id)
                .map(SlotOccupancyEntry::from)
                .orElseThrow(resourceNotFoundExceptionSupplier(ref));
    }

    @Override
    public SlotOccupancyEntry create(SlotId slotId, SnapshotId snapshotId) {
        if (slotOccupancyStore.existsBySlotIdOrSnapshotId(slotId, snapshotId)) {
            throw new AlreadyOccupiedException(String.format(
                    "Slot with ref '%s' or Snapshot with ref '%s' has already occupancy", slotId, snapshotId
            ));
        }

        var slotOccupancy = SlotOccupancy.create(
                ids.next(SlotOccupancyId.class),
                slotId,
                snapshotId
        );

        var created = slotOccupancyStore.save(slotOccupancy);

        return new SlotOccupancyEntry(
                created.getSlotId().ref(),
                created.getSnapshotId().ref()
        );
    }

    @Override
    public void remove(SlotOccupancyId id) {
        slotOccupancyStore.deleteById(id);
    }
}
