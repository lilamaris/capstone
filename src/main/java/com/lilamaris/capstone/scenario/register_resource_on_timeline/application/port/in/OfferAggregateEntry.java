package com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record OfferAggregateEntry(
        ExternalizableId id,
        JsonNode state
) {
}
