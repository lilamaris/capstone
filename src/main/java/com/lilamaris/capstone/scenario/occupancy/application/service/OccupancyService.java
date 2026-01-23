package com.lilamaris.capstone.scenario.occupancy.application.service;

import com.lilamaris.capstone.scenario.occupancy.application.port.in.OccupancyEntry;
import com.lilamaris.capstone.scenario.occupancy.application.port.in.OccupancyReader;
import com.lilamaris.capstone.scenario.occupancy.application.port.in.SlotOccupier;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyCreator;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyEntry;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyReader;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotEntry;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotReader;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.application.port.in.SlotEntry;
import com.lilamaris.capstone.timeline.application.port.in.SlotReader;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OccupancyService implements
        SlotOccupier,
        OccupancyReader {
    private final SlotOccupancyCreator slotOccupancyCreator;
    private final SlotOccupancyReader slotOccupancyReader;
    private final SlotReader slotReader;
    private final SnapshotReader snapshotReader;

    @Override
    @Transactional
    public OccupancyEntry occupy(SlotId slotId, SnapshotId snapshotId) {
        var occupancy = slotOccupancyCreator.create(slotId, snapshotId);
        var slot = slotReader.resolveRef(occupancy.slotRef());
        var snapshot = snapshotReader.resolveRef(occupancy.snapshotRef());
        return OccupancyEntry.from(slot, snapshot);
    }

    @Override
    public List<OccupancyEntry> getOccupancyFromSlotByTxTime(TimelineId timelineId, Instant at) {
        var slots = slotReader.getByTimelineInTxTime(timelineId, at).stream()
                .collect(Collectors.toMap(SlotEntry::ref, Function.identity()));
        var slotRefs = slots.keySet().stream().toList();
        var occupancy = slotOccupancyReader.getBySlotRefs(slotRefs);

        var snapshotRefs = occupancy.stream().map(SlotOccupancyEntry::snapshotRef).toList();
        var snapshots = snapshotReader.resolveRefs(snapshotRefs).stream()
                .collect(Collectors.toMap(SnapshotEntry::snapshotRef, Function.identity()));

        var occupancies = occupancy.stream().collect(Collectors.toMap(
                SlotOccupancyEntry::slotRef,
                entry -> snapshots.get(entry.snapshotRef())
        ));

        return slots.values().stream()
                .map(slot -> {
                    var snapshotRef = occupancies.get(slot.ref());
                    return OccupancyEntry.from(slot, snapshotRef);
                })
                .toList();
    }
}
