package com.lilamaris.capstone.resource_offer.infrastructure.persistence.jpa;

import com.lilamaris.capstone.resource_offer.application.port.out.ResourceOfferStore;
import com.lilamaris.capstone.resource_offer.domain.ResourceOffer;
import com.lilamaris.capstone.resource_offer.domain.id.ResourceOfferId;
import com.lilamaris.capstone.resource_offer.infrastructure.persistence.jpa.repository.ResourceOfferRepository;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ResourceOfferPersistenceAdapter implements ResourceOfferStore {
    private final ResourceOfferRepository repository;

    @Override
    public boolean isExists(DomainType resourceType, ExternalizableId resourceId, ExternalizableId snapshotId) {
        return repository.existsOffer(
                resourceType.name(),
                resourceId.asString(),
                JpaExternalizableId.from(snapshotId)
        );
    }

    @Override
    public Optional<ResourceOffer> getById(ResourceOfferId id) {
        return repository.findById(id);
    }

    @Override
    public List<ResourceOffer> getAll() {
        return repository.findAll();
    }

    @Override
    public ResourceOffer save(ResourceOffer resourceOffer) {
        return repository.save(resourceOffer);
    }

    @Override
    public void deleteById(ResourceOfferId id) {
        repository.deleteById(id);
    }
}
