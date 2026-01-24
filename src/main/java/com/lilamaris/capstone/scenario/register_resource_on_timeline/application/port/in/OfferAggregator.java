package com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface OfferAggregator {
    void aggregate(
            DomainType resourceType,
            ExternalizableId targetSlotId
    );
}
