package com.lilamaris.capstone.membership.infrastructure.persistence.jpa;

import com.lilamaris.capstone.membership.application.port.out.MembershipPort;
import com.lilamaris.capstone.membership.domain.Membership;
import com.lilamaris.capstone.membership.domain.MembershipStatus;
import com.lilamaris.capstone.membership.domain.id.MembershipId;
import com.lilamaris.capstone.membership.infrastructure.persistence.jpa.repository.MembershipRepository;
import com.lilamaris.capstone.shared.application.policy.resource.membership.port.out.MembershipQuery;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaDomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements
        MembershipPort,
        MembershipQuery {
    private final MembershipRepository repository;

    @Override
    public Optional<Membership> getById(MembershipId id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Membership> getMembership(CanonicalActor actorRef, DomainRef resourceRef) {
        return repository.findMembership(
                actorRef.type(),
                actorRef.id().asString(),
                resourceRef.type().name(),
                resourceRef.id().asString()
        );
    }

    @Override
    public Membership save(Membership domain) {
        return repository.save(domain);
    }

    @Override
    public void delete(MembershipId id) {
        repository.deleteById(id);
    }

    @Override
    public List<DomainRef> getActivated(DomainType type, CanonicalActor actor) {
        return find(type, actor, Set.of(MembershipStatus.ACTIVE));
    }

    @Override
    public List<DomainRef> getSuspended(DomainType type, CanonicalActor actor) {
        return find(type, actor, Set.of(MembershipStatus.SUSPENDED));
    }

    @Override
    public List<DomainRef> getInvited(DomainType type, CanonicalActor actor) {
        return find(type, actor, Set.of(MembershipStatus.INVITED));
    }

    private List<DomainRef> find(
            DomainType type,
            CanonicalActor actor,
            Set<MembershipStatus> statuses
    ) {
        return repository.findMemberships(
                        actor.type(),
                        actor.id().asString(),
                        type.name(),
                        statuses
                )
                .stream()
                .map(Membership::getResource)
                .map(JpaDomainRef::toPOJO)
                .toList();
    }
}
