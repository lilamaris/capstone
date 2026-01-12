package com.lilamaris.capstone.scenario.slot_occupancy.application.port.in;

import com.lilamaris.capstone.scenario.slot_occupancy.application.result.SlotOccupancyResult;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.List;

public interface SlotOccupancyQueryUseCase {
    List<SlotOccupancyResult.Query> getOccupancyFromSlotByTxTime(TimelineId timelineId, Instant at);
}
