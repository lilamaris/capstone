package com.lilamaris.capstone.scenario.occupancy.application.port.in;

import com.lilamaris.capstone.snapshot.application.port.in.SnapshotEntry;
import com.lilamaris.capstone.timeline.application.port.in.SlotEntry;
import org.springframework.lang.Nullable;

import java.util.Optional;

public record OccupancyEntry(
        SlotSummaryEntry slot,
        @Nullable SnapshotSummaryEntry snapshot
) {
    public static OccupancyEntry from(
            SlotEntry slotEntry,
            @Nullable SnapshotEntry snapshotEntry
    ) {
        var slotSummary = SlotSummaryEntry.from(slotEntry);
        var snapshotSummary = Optional.ofNullable(snapshotEntry)
                .map(SnapshotSummaryEntry::from)
                .orElse(null);

        return new OccupancyEntry(
                slotSummary,
                snapshotSummary
        );
    }
}
