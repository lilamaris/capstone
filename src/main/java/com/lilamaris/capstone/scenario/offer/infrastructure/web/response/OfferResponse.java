package com.lilamaris.capstone.scenario.offer.infrastructure.web.response;

import com.lilamaris.capstone.scenario.offer.application.port.in.OfferEntry;
import com.lilamaris.capstone.shared.infrastructure.web.response.DomainRefResponse;

public record OfferResponse(
        DomainRefResponse snapshot,
        DomainRefResponse resource
) {
    public static OfferResponse from(OfferEntry offerEntry) {
        var snapshot = DomainRefResponse.from(offerEntry.snapshotEntry().snapshotRef());
        var resource = DomainRefResponse.from(offerEntry.offeredResource());
        return new OfferResponse(
                snapshot,
                resource
        );
    }
}
