package com.lilamaris.capstone.scenario.offer.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotEntry;

public record OfferEntry(
        SnapshotEntry snapshotEntry,
        DomainRef offeredResource
) {
    public static OfferEntry from(SnapshotEntry snapshotEntry, DomainRef resourceRef) {
        return new OfferEntry(
                snapshotEntry,
                resourceRef
        );
    }
}
