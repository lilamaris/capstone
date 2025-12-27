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
    public boolean hasGrant(CanonicalActor actor, DomainRef domainRef, String scopedRole) {
        return repository.existsByActor_TypeAndActor_IdAndResource_TypeAndResource_IdAndScopedRole(
                actor.type(),
                actor.id().asString(),
                domainRef.type(),
                domainRef.id().asString(),
                scopedRole);
    }

    @Override
    public Optional<AccessControl> getById(AccessControlId id) {
        return repository.findById(id);
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
