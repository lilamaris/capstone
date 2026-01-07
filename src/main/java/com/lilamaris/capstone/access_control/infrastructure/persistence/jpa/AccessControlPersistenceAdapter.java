package com.lilamaris.capstone.access_control.infrastructure.persistence.jpa;

import com.lilamaris.capstone.shared.application.access_control.contract.AuthorizationEntry;
import com.lilamaris.capstone.access_control.application.port.out.external.AuthorizationQuery;
import com.lilamaris.capstone.shared.application.access_control.contract.ResourceMembershipEntry;
import com.lilamaris.capstone.access_control.application.port.out.external.ResourceMembershipQuery;
import com.lilamaris.capstone.access_control.application.port.out.internal.AccessControlPort;
import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.access_control.infrastructure.persistence.jpa.repository.AccessControlRepository;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccessControlPersistenceAdapter implements
        AccessControlPort,
        AuthorizationQuery,
        ResourceMembershipQuery
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
    public Optional<AuthorizationEntry> getAuthorization(CanonicalActor actor, DomainRef ref) {
        return repository.findByActor_TypeAndActor_IdAndResource_TypeAndResource_Id(
                        actor.type(),
                        actor.id().asString(),
                        ref.type(),
                        ref.id().asString()
                )
                .map(accessControl -> new AuthorizationEntry(accessControl.getScopedRole()));
    }

    @Override
    public List<ResourceMembershipEntry> getMembershipByType(CanonicalActor actor, DomainType type) {
        return repository.findByActor_TypeAndActor_IdAndResource_Type(
                        actor.type(),
                        actor.id().asString(),
                        type
                )
                .stream()
                .map(
                accessControl -> new ResourceMembershipEntry(accessControl.getResource(), accessControl.getScopedRole())
                )
                .toList();
    }
}
