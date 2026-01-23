package com.lilamaris.capstone.scenario.occupancy.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;
import com.lilamaris.capstone.timeline.application.port.in.SlotEntry;

public record SlotSummaryEntry(
        DomainRef ref,
        EffectiveMetadata tx,
        EffectiveMetadata valid
) {
    public static SlotSummaryEntry from(SlotEntry slotEntry) {
        return new SlotSummaryEntry(
                slotEntry.ref(),
                slotEntry.tx(),
                slotEntry.valid()
        );
    }
}
