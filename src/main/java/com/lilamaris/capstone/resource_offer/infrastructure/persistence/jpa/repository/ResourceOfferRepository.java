package com.lilamaris.capstone.resource_offer.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.resource_offer.domain.ResourceOffer;
import com.lilamaris.capstone.resource_offer.domain.id.ResourceOfferId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceOfferRepository extends JpaRepository<ResourceOffer, ResourceOfferId> {
}
