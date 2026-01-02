package com.lilamaris.capstone.shared.application.access_control.policy.defaults;

import com.lilamaris.capstone.access_control.application.port.out.AccessControlPort;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainAction;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainAuthorizer;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainPolicyDirectory;
import com.lilamaris.capstone.shared.application.exception.ResourceForbiddenException;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
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
