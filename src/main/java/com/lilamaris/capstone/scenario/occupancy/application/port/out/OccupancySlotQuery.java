package com.lilamaris.capstone.scenario.occupancy.application.port.out;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.List;

public interface OccupancySlotQuery {
    List<OccupancySlotEntry> getEntryByTimelineIdInTxTime(TimelineId timelineId, Instant at);

    OccupancySlotEntry getEntry(DomainRef id);
}
