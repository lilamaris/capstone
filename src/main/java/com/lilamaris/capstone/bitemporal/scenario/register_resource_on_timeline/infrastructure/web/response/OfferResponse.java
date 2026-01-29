package com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.infrastructure.web.response;

import com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.application.port.in.OfferEntry;
import com.lilamaris.capstone.shared.infrastructure.web.response.DomainRefResponse;

public record OfferResponse(
        DomainRefResponse snapshot,
        DomainRefResponse resource
) {
    public static OfferResponse from(OfferEntry offerEntry) {
        var snapshot = DomainRefResponse.from(offerEntry.slotEntry().ref());
        var resource = DomainRefResponse.from(offerEntry.offeredResource());
        return new OfferResponse(
                snapshot,
                resource
        );
    }
}
