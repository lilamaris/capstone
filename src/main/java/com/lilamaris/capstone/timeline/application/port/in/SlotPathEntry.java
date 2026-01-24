package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.SlotClosure;

public record SlotPathEntry(
        DomainRef ref,
        Integer depth
) {
    public static SlotPathEntry from(Slot slot, SlotClosure closure) {
        return new SlotPathEntry(
                slot.id().ref(),
                closure.getDepth()
        );
    }
}
