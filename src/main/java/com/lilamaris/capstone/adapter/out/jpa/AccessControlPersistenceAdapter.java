package com.lilamaris.capstone.adapter.out.jpa;

import com.lilamaris.capstone.adapter.out.jpa.repository.AccessControlRepository;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.domain.model.auth.access_control.AccessControl;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
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
