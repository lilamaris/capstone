package com.lilamaris.capstone.scenario.occupancy.application.port.in;

import com.lilamaris.capstone.scenario.occupancy.application.result.OccupancyResult;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.List;

public interface OccupancyQueryUseCase {
    List<OccupancyResult.Query> getOccupancyFromSlotByTxTime(TimelineId timelineId, Instant at);
}
