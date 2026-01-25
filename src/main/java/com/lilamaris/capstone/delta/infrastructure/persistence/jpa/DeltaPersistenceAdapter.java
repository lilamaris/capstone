package com.lilamaris.capstone.delta.infrastructure.persistence.jpa;

import com.lilamaris.capstone.delta.application.port.in.DeltaReadOption;
import com.lilamaris.capstone.delta.application.port.out.DeltaStore;
import com.lilamaris.capstone.delta.domain.Delta;
import com.lilamaris.capstone.delta.domain.id.DeltaId;
import com.lilamaris.capstone.delta.infrastructure.persistence.jpa.repository.DeltaRepository;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import com.lilamaris.capstone.shared.infrastructure.persistence.specification.DomainRefSpecification;
import com.lilamaris.capstone.shared.infrastructure.persistence.specification.ExternalizableIdSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeltaPersistenceAdapter implements DeltaStore {
    private final DeltaRepository repository;

    private Specification<Delta> resourceRefSpecification(DomainRef ref) {
        return DomainRefSpecification.eqDomainRef(
                ref,
                (root, cb) -> root.get("resource")
        );
    }

    private Specification<Delta> resourceTypeSpecification(DomainType type) {
        return DomainRefSpecification.eqDomainType(
                type,
                (root, cb) -> root.get("resource")
        );
    }

    private Specification<Delta> resourceIdSpecification(ExternalizableId id) {
        return ExternalizableIdSpecification.eqExternalizableId(
                id,
                (root, cb) -> root.get("resource")
        );
    }

    private Specification<Delta> slotIdInSpecification(List<ExternalizableId> ids) {
        return ExternalizableIdSpecification.inExternalizableIds(
                ids,
                (root, cb) -> root.get("slotId")
        );
    }

    @Override
    public boolean isExists(DomainRef resource, ExternalizableId slotId) {
        Specification<Delta> spec = Specification.unrestricted();
        spec = spec.and(resourceRefSpecification(resource));
        spec = spec.and(slotIdInSpecification(List.of(slotId)));
        return repository.exists(spec);
    }

    @Override
    public Optional<Delta> getById(DeltaId id) {
        return repository.findById(id);
    }

    @Override
    public List<Delta> getDelta(DeltaReadOption option) {
        Specification<Delta> spec = Specification.unrestricted();
        spec = spec.and(slotIdInSpecification(option.slotIds()));

        if (option.hasResourceType()) {
            spec = spec.and(resourceIdSpecification(option.resourceId()));
        }

        if (option.hasResourceId()) {
            spec = spec.and(resourceTypeSpecification(option.resourceType()));
        }

        return repository.findAll(spec);
    }

    @Override
    public Delta save(Delta delta) {
        return repository.save(delta);
    }

    @Override
    public void deleteById(DeltaId id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(DomainRef resource, ExternalizableId slotId) {
        Specification<Delta> spec = Specification.unrestricted();
        spec = spec.and(resourceRefSpecification(resource));
        spec = spec.and(slotIdInSpecification(List.of(slotId)));

        repository.delete(spec);
    }
}
