package com.lilamaris.capstone.access_control.application.port.out.external;

import com.lilamaris.capstone.shared.application.access_control.contract.AuthorizationEntry;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.util.Optional;

public interface AuthorizationQuery {
    Optional<AuthorizationEntry> getAuthorization(CanonicalActor actor, DomainRef ref);
}
