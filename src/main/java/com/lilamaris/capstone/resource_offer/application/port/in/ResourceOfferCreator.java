package com.lilamaris.capstone.resource_offer.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface ResourceOfferCreator {
    ResourceOfferEntry issue(DomainRef resource, ExternalizableId snapshotId, String jsonPatch);
}
