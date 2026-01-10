package com.lilamaris.capstone.membership.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.membership.domain.Membership;
import com.lilamaris.capstone.membership.domain.MembershipStatus;
import com.lilamaris.capstone.membership.domain.id.MembershipId;
import com.lilamaris.capstone.shared.domain.event.actor.ActorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MembershipRepository extends JpaRepository<Membership, MembershipId> {
    @Query("""
            SELECT m
            FROM Membership m
            WHERE m.actor.type = :actorType
                AND m.actor.id = :actorId
                AND m.resource.type.name = :resourceType
                AND m.resource.id = :resourceId
            """)
    Optional<Membership> findMembership(
            ActorType actorType,
            String actorId,
            String resourceType,
            String resourceId
    );

    @Query("""
            SELECT m
            FROM Membership m
            WHERE m.actor.type = :actorType
                AND m.actor.id = :actorId
                AND m.resource.type.name = :resourceType
                AND m.status IN :status
            """)
    List<Membership> findMemberships(
            ActorType actorType,
            String actorId,
            String resourceType,
            Set<MembershipStatus> statuses
    );
}
