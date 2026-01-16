package com.lilamaris.capstone.resource_offer.application.port.out;

import com.lilamaris.capstone.resource_offer.domain.ResourceOffer;
import com.lilamaris.capstone.resource_offer.domain.id.ResourceOfferId;

import java.util.List;
import java.util.Optional;

public interface ResourceOfferStore {
    Optional<ResourceOffer> getById(ResourceOfferId id);

    List<ResourceOffer> getAll();

    ResourceOffer save(ResourceOffer resourceOffer);

    void deleteById(ResourceOfferId id);
}
