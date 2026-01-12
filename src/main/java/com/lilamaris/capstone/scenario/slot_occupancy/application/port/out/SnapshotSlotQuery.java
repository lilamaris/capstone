package com.lilamaris.capstone.scenario.slot_occupancy.application.port.out;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.List;

public interface SnapshotSlotQuery {
    List<SnapshotSlotEntry> getEntryByTimelineIdInTxTime(TimelineId timelineId, Instant at);

    SnapshotSlotEntry getEntry(DomainRef id);
}
