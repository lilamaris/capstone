package com.lilamaris.capstone.scenario.offer.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;

public interface OfferIssuer {
    OfferEntry offer(
            DomainType resourceType,
            ExternalizableId resourceId,
            SnapshotId snapshotId
    );
}
