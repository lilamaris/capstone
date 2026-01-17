package com.lilamaris.capstone.resource_offer.application.port.out;

import com.lilamaris.capstone.resource_offer.domain.ResourceOffer;
import com.lilamaris.capstone.resource_offer.domain.id.ResourceOfferId;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;
import java.util.Optional;

public interface ResourceOfferStore {
    boolean isExists(DomainType resourceType, ExternalizableId resourceId, ExternalizableId snapshotId);

    Optional<ResourceOffer> getById(ResourceOfferId id);

    List<ResourceOffer> getAll();

    ResourceOffer save(ResourceOffer resourceOffer);

    void deleteById(ResourceOfferId id);
}
