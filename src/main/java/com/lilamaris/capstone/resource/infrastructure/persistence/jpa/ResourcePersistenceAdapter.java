package com.lilamaris.capstone.resource.infrastructure.persistence.jpa;

import com.lilamaris.capstone.resource.application.port.out.ResourceReadOption;
import com.lilamaris.capstone.resource.application.port.out.ResourceStore;
import com.lilamaris.capstone.resource.domain.Resource;
import com.lilamaris.capstone.resource.infrastructure.persistence.jpa.repository.ResourceRepository;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import com.lilamaris.capstone.shared.infrastructure.persistence.specification.DomainRefSpecification;
import com.lilamaris.capstone.shared.infrastructure.persistence.specification.ExternalizableIdSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResourcePersistenceAdapter implements ResourceStore {
    private final ResourceRepository repository;

    private Specification<Resource> resourceTypeSpecification(DomainType type) {
        return DomainRefSpecification.eqDomainType(
                type,
                (root, cb) -> root.get("resource")
        );
    }

    private Specification<Resource> resourceIdSpecification(ExternalizableId id) {
        return ExternalizableIdSpecification.eqExternalizableId(
                id,
                (root, cb) -> root.get("resource")
        );
    }

    private Specification<Resource> buildSpecification(ResourceReadOption option) {
        Specification<Resource> spec = Specification.unrestricted();
        spec = spec.and(resourceTypeSpecification(option.type()));
        if (option.hasId()) spec = spec.and(resourceIdSpecification(option.id()));
        return spec;
    }

    @Override
    public boolean isExists(ResourceReadOption option) {
        var spec = buildSpecification(option);
        return repository.exists(spec);
    }

    @Override
    public List<Resource> get(ResourceReadOption option) {
        var spec = buildSpecification(option);
        return repository.findAll(spec);
    }

    @Override
    public Resource save(Resource save) {
        return null;
    }
}
