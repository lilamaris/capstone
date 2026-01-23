package com.lilamaris.capstone.scenario.offer.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.timeline.application.port.in.SlotEntry;

public record OfferEntry(
        SlotEntry slotEntry,
        DomainRef offeredResource
) {
    public static OfferEntry from(SlotEntry slotEntry, DomainRef resourceRef) {
        return new OfferEntry(
                slotEntry,
                resourceRef
        );
    }
}
