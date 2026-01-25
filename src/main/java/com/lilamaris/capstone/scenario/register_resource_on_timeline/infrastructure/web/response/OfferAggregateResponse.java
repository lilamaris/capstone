package com.lilamaris.capstone.scenario.register_resource_on_timeline.infrastructure.web.response;

import com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in.OfferAggregateEntry;

public record OfferAggregateResponse(
        String resourceId,
        Object value
) {
    public static OfferAggregateResponse from(OfferAggregateEntry offerAggregateEntry) {
        return new OfferAggregateResponse(
                offerAggregateEntry.id().asString(),
                offerAggregateEntry.state()
        );
    }
}
