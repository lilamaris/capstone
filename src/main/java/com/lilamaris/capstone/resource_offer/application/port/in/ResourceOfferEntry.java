package com.lilamaris.capstone.resource_offer.application.port.in;

import com.lilamaris.capstone.resource_offer.domain.ResourceOffer;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public record ResourceOfferEntry(
        ExternalizableId resourceOfferId,
        DomainRef resource,
        ExternalizableId snapshotId,
        String jsonPatch
) {
    public static ResourceOfferEntry from(ResourceOffer resourceOffer) {
        return new ResourceOfferEntry(
                resourceOffer.id(),
                resourceOffer.getResource(),
                resourceOffer.getSnapshotId(),
                resourceOffer.getJsonPatch()
        );
    }
}
