package com.lilamaris.capstone.delta.infrastructure.persistence.jpa;

import com.lilamaris.capstone.delta.application.port.out.DeltaStore;
import com.lilamaris.capstone.delta.domain.Delta;
import com.lilamaris.capstone.delta.domain.id.DeltaId;
import com.lilamaris.capstone.delta.infrastructure.persistence.jpa.repository.DeltaRepository;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
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

    private Specification<Delta> domainRefSpecification(DomainRef ref) {
        return DomainRefSpecification.eqDomainRef(
                ref,
                (root, cb) -> root.get("resource")
        );
    }

    private Specification<Delta> externalizableIdSpecification(ExternalizableId id) {
        return ExternalizableIdSpecification.eqExternalizableId(
                id,
                (root, cb) -> root.get("slotId")
        );
    }

    @Override
    public boolean isExists(DomainRef resource, ExternalizableId slotId) {
        Specification<Delta> spec = Specification.unrestricted();
        spec = spec.and(domainRefSpecification(resource));
        spec = spec.and(externalizableIdSpecification(slotId));
        return repository.exists(spec);
    }

    @Override
    public Optional<Delta> getById(DeltaId id) {
        return repository.findById(id);
    }

    @Override
    public List<Delta> getByIds(List<DeltaId> ids) {
        return repository.findAllById(ids);
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
        spec = spec.and(domainRefSpecification(resource));
        spec = spec.and(externalizableIdSpecification(slotId));

        repository.delete(spec);
    }
}
