package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;
import com.lilamaris.capstone.timeline.domain.Slot;

public record SlotEntry(
        DomainRef ref,
        EffectiveMetadata tx,
        EffectiveMetadata valid
) {
    public static SlotEntry from(Slot slot) {
        return new SlotEntry(
                slot.id().ref(),
                slot.getTx(),
                slot.getValid()
        );
    }
}
