package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.domain.model.auth.access_control.AccessControl;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.ActorType;
import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;
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
