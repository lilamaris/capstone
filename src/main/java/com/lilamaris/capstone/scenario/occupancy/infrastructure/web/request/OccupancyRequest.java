package com.lilamaris.capstone.scenario.occupancy.infrastructure.web.request;

import java.util.UUID;

public record OccupancyRequest(
        UUID snapshotId
) {
}
