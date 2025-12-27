package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.domain.model.auth.access_control.AccessControl;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.ActorType;
import com.lilamaris.capstone.domain.model.common.domain.type.DomainType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessControlRepository extends JpaRepository<AccessControl, AccessControlId> {
    boolean existsByActor_TypeAndActor_IdAndResource_TypeAndResource_IdAndScopedRole(
            ActorType type,
            String actorId,
            DomainType resourceType,
            String resourceId,
            String scopedRole
    );
}
