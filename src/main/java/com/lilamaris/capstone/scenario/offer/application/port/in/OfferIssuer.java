package com.lilamaris.capstone.scenario.offer.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

public interface OfferIssuer {
    OfferEntry offer(
            DomainType resourceType,
            ExternalizableId resourceId,
            ExternalizableId snapshotId
    );
}
