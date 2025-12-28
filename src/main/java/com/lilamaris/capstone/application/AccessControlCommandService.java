package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.policy.access_control.DomainPolicyDirectory;
import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.port.in.AccessControlUseCase;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.domain.model.auth.access_control.AccessControl;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.common.domain.event.canonical.ResourceGranted;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccessControlCommandService implements AccessControlUseCase {
    private final AccessControlPort accessControlPort;
    private final IdGenerationContext ids;
    private final DomainPolicyDirectory policyDirectory;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void onResourceGranted(ResourceGranted e) {
        var domain = AccessControl.create(
                e.grantee(),
                e.ref(),
                e.scopedRole(),
                () -> ids.next(AccessControlId.class)
        );
        accessControlPort.save(domain);
    }
}
