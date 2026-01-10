package com.lilamaris.capstone.shared.application.policy.access_control.defaults;

import com.lilamaris.capstone.shared.application.exception.ResourceForbiddenException;
import com.lilamaris.capstone.shared.application.policy.access_control.port.in.ResourceAccessPolicyDirectory;
import com.lilamaris.capstone.shared.application.policy.access_control.port.in.ResourceAction;
import com.lilamaris.capstone.shared.application.policy.access_control.port.in.ResourceAuthorizer;
import com.lilamaris.capstone.shared.application.policy.access_control.port.out.ResourceAuthorityQuery;
import com.lilamaris.capstone.shared.application.policy.role.port.in.RoleResolver;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultResourceAuthorizer implements ResourceAuthorizer {
    private final ResourceAuthorityQuery authorityRepository;
    private final ResourceAccessPolicyDirectory policyDirectory;
    private final RoleResolver roleResolver;

    @Override
    public void authorize(CanonicalActor actor, DomainRef ref, ResourceAction action) {
        var actorAuthority = authorityRepository.getAuthorization(actor, ref)
                .orElseThrow(ResourceForbiddenException::new);
        var actorRole = roleResolver.parse(ref.type(), actorAuthority.scopedRole());

        var policy = policyDirectory.policyOf(ref.type());
        var requiredRoles = policy.requiredRoles(action);

        var allowed = requiredRoles.stream()
                .anyMatch(requiredRole -> roleResolver.atLeast(ref.type(), actorRole, requiredRole));

        if (!allowed) {
            throw new ResourceForbiddenException();
        }
    }
}
