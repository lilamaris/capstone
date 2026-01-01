package com.lilamaris.capstone.access_control.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.shared.domain.event.actor.ActorType;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessControlRepository extends JpaRepository<AccessControl, AccessControlId> {
    Optional<AccessControl> findByActor_TypeAndActor_IdAndResource_TypeAndResource_Id(
            ActorType type,
            String actorId,
            DomainType resourceType,
            String resourceId
    );
}
