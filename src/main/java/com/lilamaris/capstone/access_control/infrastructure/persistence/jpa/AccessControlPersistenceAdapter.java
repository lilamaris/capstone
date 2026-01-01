package com.lilamaris.capstone.access_control.infrastructure.persistence.jpa;

import com.lilamaris.capstone.access_control.application.port.out.AccessControlPort;
import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.access_control.infrastructure.persistence.jpa.repository.AccessControlRepository;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccessControlPersistenceAdapter implements AccessControlPort {
    private final AccessControlRepository repository;

    @Override
    public Optional<AccessControl> getById(AccessControlId id) {
        return repository.findById(id);
    }

    @Override
    public Optional<AccessControl> getBy(CanonicalActor actor, DomainRef ref) {
        return repository.findByActor_TypeAndActor_IdAndResource_TypeAndResource_Id(
                actor.type(),
                actor.id().asString(),
                ref.type(),
                ref.id().asString()
        );
    }

    @Override
    public AccessControl save(AccessControl domain) {
        return repository.save(domain);
    }

    @Override
    public void delete(AccessControlId id) {
        repository.deleteById(id);
    }
}
