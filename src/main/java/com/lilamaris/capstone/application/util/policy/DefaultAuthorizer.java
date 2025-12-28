package com.lilamaris.capstone.application.util.policy;

import com.lilamaris.capstone.application.exception.ResourceForbiddenException;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultAuthorizer implements DomainAuthorizer {
    private final AccessControlPort accessControlPort;
    private final DomainPolicy registry;
    private final RoleTranslator roleTranslator;

    @Override
    public void authorize(CanonicalActor actor, DomainRef ref, DomainAction action) {
        var accessControl = accessControlPort.getBy(actor, ref)
                .orElseThrow(ResourceForbiddenException::new);
        var role = roleTranslator.toRoleName(accessControl.getScopedRole());
        if (!registry.allows(role, action)) {
            throw new ResourceForbiddenException();
        }
    }
}
