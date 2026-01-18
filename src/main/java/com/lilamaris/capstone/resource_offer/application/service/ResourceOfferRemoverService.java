package com.lilamaris.capstone.resource_offer.application.service;

import com.lilamaris.capstone.resource_offer.application.port.in.ResourceOfferEntry;
import com.lilamaris.capstone.resource_offer.application.port.in.ResourceOfferRemover;
import com.lilamaris.capstone.resource_offer.application.port.out.ResourceOfferStore;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceOfferRemoverService implements ResourceOfferRemover {
    private final ResourceOfferStore resourceOfferStore;

    @Override
    public ResourceOfferEntry revoke(DomainRef resource, ExternalizableId externalSnapshotId) {
        var snapshotId = DefaultExternalizableId.from(externalSnapshotId);
        var resourceOffer = resourceOfferStore.get(
                resource.type(),
                resource.id(),
                snapshotId
        ).orElseThrow(() -> new ResourceNotFoundException(String.format(
                "ResourceOffer with resource type '%s' and id '%s' in Snapshot with id '%s' not found.",
                resource.type(),
                resource.id().asString(),
                snapshotId.asString()
        )));
        resourceOfferStore.deleteById(resourceOffer.id());
        return ResourceOfferEntry.from(resourceOffer);
    }
}
