package com.lilamaris.capstone.application.util.policy.defaults;

import com.lilamaris.capstone.application.exception.ResourceForbiddenException;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.application.util.policy.DomainAction;
import com.lilamaris.capstone.application.util.policy.DomainAuthorizer;
import com.lilamaris.capstone.application.util.policy.DomainPolicyDirectory;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultAuthorizer implements DomainAuthorizer {
    private final AccessControlPort accessControlPort;
    private final DomainPolicyDirectory domainPolicyDirectory;

    @Override
    public void authorize(CanonicalActor actor, DomainRef ref, DomainAction action) {
        var accessControl = accessControlPort.getBy(actor, ref)
                .orElseThrow(ResourceForbiddenException::new);
        var policy = domainPolicyDirectory.policyOf(ref.type());
        var role = policy.parseRole(accessControl.getScopedRole());
        if (!policy.allows(role, action)) {
            throw new ResourceForbiddenException();
        }
    }
}
