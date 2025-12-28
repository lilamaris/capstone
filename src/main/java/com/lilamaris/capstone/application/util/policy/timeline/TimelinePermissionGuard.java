package com.lilamaris.capstone.application.util.policy.timeline;

import com.lilamaris.capstone.application.exception.ResourceForbiddenException;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.application.util.policy.DomainAction;
import com.lilamaris.capstone.application.util.policy.DomainPermissionGuard;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimelinePermissionGuard implements DomainPermissionGuard {
    private final AccessControlPort accessControlPort;

    private final TimelinePermissionRegistry registry;
    private final TimelineRoleTranslator roleTranslator;

    @Override
    public void check(CanonicalActor actor, DomainRef ref, DomainAction action) {
        var accessControl = accessControlPort.getBy(actor, ref)
                .orElseThrow(ResourceForbiddenException::new);
        var role = roleTranslator.toRoleName(accessControl.getScopedRole());
        if (!registry.can(role, action)) {
            throw new ResourceForbiddenException();
        }
    }
}
