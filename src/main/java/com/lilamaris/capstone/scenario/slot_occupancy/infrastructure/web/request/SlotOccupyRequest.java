package com.lilamaris.capstone.scenario.slot_occupancy.infrastructure.web.request;

import java.util.UUID;

public record SlotOccupyRequest(
        UUID snapshotId
) {
}
