package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.model.auth.access_control.AccessControl;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;

import java.util.Optional;

public interface AccessControlPort {
    Optional<AccessControl> getById(AccessControlId id);

    Optional<AccessControl> getBy(CanonicalActor actor, DomainRef ref);

    AccessControl save(AccessControl domain);

    void delete(AccessControlId id);
}
