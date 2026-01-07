package com.lilamaris.capstone.access_control.application.port.out.internal;

import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;
import java.util.Optional;

public interface AccessControlPort {
    Optional<AccessControl> getById(AccessControlId id);

    Optional<AccessControl> getByActorAndRef(CanonicalActor actor, DomainRef ref);

    List<AccessControl> getByActorAndType(CanonicalActor actor, DomainType type);

    AccessControl save(AccessControl domain);

    void delete(AccessControlId id);
}
