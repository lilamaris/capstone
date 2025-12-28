package com.lilamaris.capstone.application.util.policy;

import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;

public interface DomainPermissionGuard {
    void check(CanonicalActor actor, DomainRef ref, DomainAction action);
}
