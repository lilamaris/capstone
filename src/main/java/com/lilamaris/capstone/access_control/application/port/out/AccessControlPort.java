package com.lilamaris.capstone.access_control.application.port.out;

import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.util.Optional;

public interface AccessControlPort {
    Optional<AccessControl> getById(AccessControlId id);

    Optional<AccessControl> getBy(CanonicalActor actor, DomainRef ref);

    AccessControl save(AccessControl domain);

    void delete(AccessControlId id);
}
