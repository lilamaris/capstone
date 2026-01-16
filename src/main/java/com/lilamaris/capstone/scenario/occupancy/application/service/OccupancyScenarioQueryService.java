package com.lilamaris.capstone.scenario.occupancy.application.service;

import com.lilamaris.capstone.scenario.occupancy.application.port.in.OccupancyQueryUseCase;
import com.lilamaris.capstone.scenario.occupancy.application.port.out.*;
import com.lilamaris.capstone.scenario.occupancy.application.result.OccupancyResult;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OccupancyScenarioQueryService implements OccupancyQueryUseCase {
    private final SlotOccupancyQuery slotOccupancyQuery;
    private final OccupancySlotQuery occupancySlotQuery;
    private final OccupancySnapshotQuery occupancySnapshotQuery;

    @Override
    public List<OccupancyResult.Query> getOccupancyFromSlotByTxTime(TimelineId timelineId, Instant at) {

        var slots = occupancySlotQuery.getEntryByTimelineIdInTxTime(timelineId, at).stream()
                .collect(Collectors.toMap(OccupancySlotEntry::id, Function.identity()));
        var slotIds = slots.keySet().stream().toList();
        var occupiedSlots = slotOccupancyQuery.getOccupanciesBySlotIds(slotIds);

        var occupiedSnapshotRefs = occupiedSlots.stream().map(SlotOccupancyEntry::snapshotRef).toList();
        var occupiedSnapshots = occupancySnapshotQuery.getEntries(occupiedSnapshotRefs).stream()
                .collect(Collectors.toMap(OccupancySnapshotEntry::id, Function.identity()));

        var occupancies = occupiedSlots.stream().collect(Collectors.toMap(
                entry -> entry.snapshotSlotRef().id(),
                entry -> occupiedSnapshots.get(entry.snapshotRef().id())
        ));

        return slots.values().stream()
                .map(slot -> OccupancyResult.Query.from(
                        slot,
                        occupancies.get(slot.id())
                ))
                .toList();
    }
}
