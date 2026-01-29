package com.lilamaris.capstone.bitemporal.timeline.application.port.in;

import com.lilamaris.capstone.bitemporal.timeline.domain.Slot;
import com.lilamaris.capstone.bitemporal.timeline.domain.SlotClosure;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

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
