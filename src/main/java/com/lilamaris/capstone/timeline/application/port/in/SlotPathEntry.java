package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.timeline.domain.SlotClosure;

public record SlotPathEntry(
        DomainRef ancestorSlotRef,
        DomainRef descendantSlotRef,
        Integer depth
) {
    public static SlotPathEntry from(SlotClosure slotClosure) {
        return new SlotPathEntry(
                slotClosure.getAncestorSlotId().ref(),
                slotClosure.getDescendantSlotId().ref(),
                slotClosure.getDepth()
        );
    }
}
