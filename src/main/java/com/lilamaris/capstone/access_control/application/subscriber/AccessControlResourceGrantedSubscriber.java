package com.lilamaris.capstone.access_control.application.subscriber;

import com.lilamaris.capstone.access_control.application.port.out.AccessControlPort;
import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceGranted;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AccessControlResourceGrantedSubscriber {
    private final AccessControlPort accessControlPort;
    private final IdGenerationDirectory ids;

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
