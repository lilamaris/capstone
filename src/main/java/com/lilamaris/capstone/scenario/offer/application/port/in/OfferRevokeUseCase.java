package com.lilamaris.capstone.scenario.offer.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface OfferRevokeUseCase {
    void revoke(DomainRef resource, ExternalizableId snapshotId);
}
