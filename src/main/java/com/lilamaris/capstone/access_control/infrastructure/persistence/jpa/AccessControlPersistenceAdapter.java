package com.lilamaris.capstone.access_control.infrastructure.persistence.jpa;

import com.lilamaris.capstone.access_control.application.port.out.AccessControlPort;
import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.access_control.infrastructure.persistence.jpa.repository.AccessControlRepository;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.out.ResourceAuthorityEntry;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.out.ResourceAuthorityQuery;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccessControlPersistenceAdapter implements
        AccessControlPort,
        ResourceAuthorityQuery
{
    private final AccessControlRepository repository;

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

    @Override
    public Optional<ResourceAuthorityEntry> getAuthorization(CanonicalActor actor, DomainRef ref) {
        var actorType = actor.type();
        var actorId = actor.id().asString();
        var resourceType = ref.type().name();
        var resourceId = ref.id().asString();

        return repository.findByActor_TypeAndActor_IdAndResource_Type_NameAndResource_Id(
                        actorType,
                        actorId,
                        resourceType,
                        resourceId
                )
                .map(accessControl -> new ResourceAuthorityEntry(accessControl.getScopedRole()));
    }
}
