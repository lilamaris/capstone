package com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;

public interface OfferAggregator {
    List<OfferAggregateEntry> aggregate(
            DomainType resourceType,
            ExternalizableId targetSlotId
    );
}
