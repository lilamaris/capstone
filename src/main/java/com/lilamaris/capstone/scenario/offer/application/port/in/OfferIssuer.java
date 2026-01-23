package com.lilamaris.capstone.scenario.offer.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface OfferIssuer {
    OfferEntry offer(
            DomainRef resource,
            ExternalizableId slotId
    );
}
