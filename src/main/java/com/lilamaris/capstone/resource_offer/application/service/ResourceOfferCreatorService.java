package com.lilamaris.capstone.resource_offer.application.service;

import com.lilamaris.capstone.resource_offer.application.port.in.ResourceOfferEntry;
import com.lilamaris.capstone.resource_offer.application.port.in.ResourceOfferCreator;
import com.lilamaris.capstone.resource_offer.application.port.out.ResourceOfferStore;
import com.lilamaris.capstone.resource_offer.domain.ResourceOffer;
import com.lilamaris.capstone.resource_offer.domain.id.ResourceOfferId;
import com.lilamaris.capstone.shared.application.exception.ResourceAlreadyExistsException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceOfferCreatorService implements ResourceOfferCreator {
    private final ResourceOfferStore resourceOfferStore;
    private final IdGenerationDirectory ids;

    public ResourceOfferEntry issue(
            DomainRef resource,
            ExternalizableId externalSnapshotId,
            String jsonPatch
    ) {
        var exists = resourceOfferStore.isExists(
                resource.type(),
                resource.id(),
                externalSnapshotId
        );
        if (exists) {
            throw new ResourceAlreadyExistsException(String.format(
                    "Resource offer with resource type '%s' and id '%s' is already offered in snapshot with id '%s'",
                    resource.type(),
                    resource.id().asString(),
                    externalSnapshotId.asString()
            ));
        }
        var resourceOffer = ResourceOffer.create(
                ids.next(ResourceOfferId.class),
                resource,
                externalSnapshotId,
                jsonPatch
        );

        var created = resourceOfferStore.save(resourceOffer);

        return ResourceOfferEntry.from(created);
    }
}
