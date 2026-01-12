package com.lilamaris.capstone.scenario.slot_occupancy.application.service;

import com.lilamaris.capstone.scenario.slot_occupancy.application.port.in.SlotOccupancyQueryUseCase;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.*;
import com.lilamaris.capstone.scenario.slot_occupancy.application.result.SlotOccupancyResult;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SlotOccupancyScenarioQueryService implements SlotOccupancyQueryUseCase {
    private final SlotOccupancyQuery slotOccupancyQuery;
    private final SnapshotSlotQuery snapshotSlotQuery;
    private final SnapshotQuery snapshotQuery;

    @Override
    public List<SlotOccupancyResult.Query> getOccupancyFromSlotByTxTime(TimelineId timelineId, Instant at) {

        var slots = snapshotSlotQuery.getEntryByTimelineIdInTxTime(timelineId, at).stream()
                .collect(Collectors.toMap(SnapshotSlotEntry::id, Function.identity()));
        var slotIds = slots.keySet().stream().toList();
        var occupiedSlots = slotOccupancyQuery.getOccupanciesBySlotIds(slotIds);

        var occupiedSnapshotRefs = occupiedSlots.stream().map(SlotOccupancyEntry::snapshotRef).toList();
        var occupiedSnapshots = snapshotQuery.getEntries(occupiedSnapshotRefs).stream()
                .collect(Collectors.toMap(SnapshotEntry::id, Function.identity()));

        var occupancies = occupiedSlots.stream().collect(Collectors.toMap(
                entry -> entry.snapshotSlotRef().id(),
                entry -> occupiedSnapshots.get(entry.snapshotRef().id())
        ));

        return slots.values().stream()
                .map(slot -> SlotOccupancyResult.Query.from(
                        slot,
                        occupancies.get(slot.id())
                ))
                .toList();
    }
}
