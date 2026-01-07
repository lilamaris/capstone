package com.lilamaris.capstone.shared.application.access_control.defaults;

import com.lilamaris.capstone.access_control.application.port.out.external.AuthorizationQuery;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainAction;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainAuthorizer;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainPolicyDirectory;
import com.lilamaris.capstone.shared.application.exception.ResourceForbiddenException;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAuthorizer implements DomainAuthorizer {
    private final AuthorizationQuery authorizationQuery;
    private final DomainPolicyDirectory domainPolicyDirectory;

    @Override
    public void authorize(CanonicalActor actor, DomainRef ref, DomainAction action) {
        var auth = authorizationQuery.getAuthorization(actor, ref)
                .orElseThrow(ResourceForbiddenException::new);
        var policy = domainPolicyDirectory.policyOf(ref.type());
        var role = policy.parseRole(auth.scopedRole());
        if (!policy.allows(role, action)) {
            throw new ResourceForbiddenException();
        }
    }
}
