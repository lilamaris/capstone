package com.lilamaris.capstone.scenario.occupancy.infrastructure.web.response;

import com.lilamaris.capstone.scenario.occupancy.application.port.in.OccupancyEntry;
import org.springframework.lang.Nullable;

public record OccupancyResponse(
        SlotSummaryResponse slot,
        @Nullable SnapshotSummaryResponse snapshot
) {
    public static OccupancyResponse from(OccupancyEntry occupancyEntry) {
        return new OccupancyResponse(
                SlotSummaryResponse.from(occupancyEntry.slot()),
                SnapshotSummaryResponse.from(occupancyEntry.snapshot())
        );
    }
}
