package com.lilamaris.capstone.resource_offer.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface ResourceOfferRemover {
    ResourceOfferEntry revoke(DomainRef resource, ExternalizableId externalSnapshotId);
}
