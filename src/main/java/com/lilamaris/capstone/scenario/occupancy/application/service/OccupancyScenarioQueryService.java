package com.lilamaris.capstone.scenario.occupancy.application.service;

import com.lilamaris.capstone.scenario.occupancy.application.port.in.OccupancyQueryUseCase;
import com.lilamaris.capstone.scenario.occupancy.application.result.OccupancyResult;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyEntry;
import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotOccupancyReader;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotEntry;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotReader;
import com.lilamaris.capstone.timeline.application.port.in.SlotEntry;
import com.lilamaris.capstone.timeline.application.port.in.SlotReader;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OccupancyScenarioQueryService implements OccupancyQueryUseCase {
    private final SlotReader slotReader;
    private final SlotOccupancyReader slotOccupancyReader;
    private final SnapshotReader snapshotReader;

    @Override
    public List<OccupancyResult.Query> getOccupancyFromSlotByTxTime(TimelineId timelineId, Instant at) {
        var slots = slotReader.getByTimelineInTxTime(timelineId, at).stream()
                .collect(Collectors.toMap(SlotEntry::ref, Function.identity()));
        var slotRefs = slots.keySet().stream().toList();
        var occupancy = slotOccupancyReader.getBySlotRefs(slotRefs);

        var snapshotRefs = occupancy.stream().map(SlotOccupancyEntry::snapshotRef).toList();
        var snapshots = snapshotReader.resolveRefs(snapshotRefs).stream()
                .collect(Collectors.toMap(SnapshotEntry::ref, Function.identity()));

        var occupancies = occupancy.stream().collect(Collectors.toMap(
                SlotOccupancyEntry::slotRef,
                entry -> snapshots.get(entry.snapshotRef())
        ));

        return slots.values().stream()
                .map(slot -> {
                    var snapshotId = Optional.ofNullable(occupancies.get(slot.ref()))
                            .map(e -> e.ref().id())
                            .orElse(null);
                    return OccupancyResult.Query.from(slot.tx(), slot.valid(), snapshotId);
                })
                .toList();
    }
}
