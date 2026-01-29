package com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface OfferIssuer {
    OfferEntry offer(
            DomainRef resource,
            ExternalizableId targetSlotId
    );
}
