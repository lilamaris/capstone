package com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.infrastructure.web.request;

import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;

public class OfferRequest {
    public record Offer(
            AggregateDomainType resourceType,
            String resourceId,
            String slotId
    ) {
    }

    public record Aggregate(
            AggregateDomainType resourceType,
            String slotId
    ) {
    }
}
