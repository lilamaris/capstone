package com.lilamaris.capstone.scenario.slot_occupancy.application.result;

import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SnapshotEntry;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SnapshotSlotEntry;
import com.lilamaris.capstone.shared.application.result.DescriptionResult;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Optional;

public class SlotOccupancyResult {
    public record Query(
            SlotSummary slot,
            @Nullable SnapshotSummary snapshot
    ) {
        public static Query from(SnapshotSlotEntry slotEntry, SnapshotEntry snapshotEntry) {
            var slotSummary = new SlotSummary(slotEntry.id(), slotEntry.validFrom(), slotEntry.validTo());
            var snapshotSummary = Optional.ofNullable(snapshotEntry)
                    .map(s -> new SnapshotSummary(s.id(), s.description()))
                    .orElse(null);
            return new Query(slotSummary, snapshotSummary);
        }
    }

    public record Command(
            SlotSummary slot,
            SnapshotSummary snapshotSummary
    ) {
        public static Command from(SnapshotSlotEntry slotEntry, SnapshotEntry snapshotEntry) {
            var slotSummary = new SlotSummary(slotEntry.id(), slotEntry.validFrom(), slotEntry.validTo());
            var snapshotSummary = new SnapshotSummary(snapshotEntry.id(), snapshotEntry.description());
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
