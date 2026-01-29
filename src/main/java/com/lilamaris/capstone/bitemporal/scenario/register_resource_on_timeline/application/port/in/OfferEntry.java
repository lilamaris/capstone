package com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.application.port.in;

import com.lilamaris.capstone.bitemporal.timeline.application.port.in.SlotEntry;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

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
