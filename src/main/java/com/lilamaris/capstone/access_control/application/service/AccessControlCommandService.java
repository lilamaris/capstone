package com.lilamaris.capstone.access_control.application.service;

import com.lilamaris.capstone.access_control.application.port.in.AccessControlUseCase;
import com.lilamaris.capstone.access_control.application.port.in.DomainPolicyDirectory;
import com.lilamaris.capstone.access_control.application.port.out.AccessControlPort;
import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.shared.application.identity.IdGenerationContext;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceGranted;
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
                ids.next(AccessControlId.class)
        );
        accessControlPort.save(domain);
    }
}
