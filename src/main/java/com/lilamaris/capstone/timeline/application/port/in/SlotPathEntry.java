package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.timeline.domain.SlotClosure;

public record SlotPathEntry(
        ExternalizableId ancestorSlotId,
        ExternalizableId descendantSlotId,
        Integer depth
) {
    public static SlotPathEntry from(SlotClosure slotClosure) {
        return new SlotPathEntry(
                slotClosure.getAncestorSlotId(),
                slotClosure.getDescendantSlotId(),
                slotClosure.getDepth()
        );
    }
}
