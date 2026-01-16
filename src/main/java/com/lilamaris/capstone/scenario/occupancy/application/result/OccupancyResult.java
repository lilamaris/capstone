package com.lilamaris.capstone.scenario.occupancy.application.result;

import com.lilamaris.capstone.scenario.occupancy.application.port.out.OccupancySnapshotEntry;
import com.lilamaris.capstone.scenario.occupancy.application.port.out.OccupancySlotEntry;
import com.lilamaris.capstone.shared.application.result.DescriptionResult;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Optional;

public class OccupancyResult {
    public record Query(
            SlotSummary slot,
            @Nullable SnapshotSummary snapshot
    ) {
        public static Query from(OccupancySlotEntry slotEntry, OccupancySnapshotEntry occupancySnapshotEntry) {
            var slotSummary = new SlotSummary(slotEntry.id(), slotEntry.validFrom(), slotEntry.validTo());
            var snapshotSummary = Optional.ofNullable(occupancySnapshotEntry)
                    .map(s -> new SnapshotSummary(s.id(), s.description()))
                    .orElse(null);
            return new Query(slotSummary, snapshotSummary);
        }
    }

    public record Command(
            SlotSummary slot,
            SnapshotSummary snapshotSummary
    ) {
        public static Command from(OccupancySlotEntry slotEntry, OccupancySnapshotEntry occupancySnapshotEntry) {
            var slotSummary = new SlotSummary(slotEntry.id(), slotEntry.validFrom(), slotEntry.validTo());
            var snapshotSummary = new SnapshotSummary(occupancySnapshotEntry.id(), occupancySnapshotEntry.description());
            return new Command(slotSummary, snapshotSummary);
        }
    }

    private record SlotSummary(
            ExternalizableId id,
            Instant validFrom,
            Instant validTo
    ) {
    }

    private record SnapshotSummary(
            ExternalizableId id,
            DescriptionResult description
    ) {

    }
}
