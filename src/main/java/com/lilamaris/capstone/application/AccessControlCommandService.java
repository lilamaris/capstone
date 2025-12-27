package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.port.in.AccessControlUseCase;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.domain.model.auth.access_control.AccessControl;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.common.event.canonical.ResourceCreated;
import com.lilamaris.capstone.domain.model.common.event.actor.UserActor;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessControlCommandService implements AccessControlUseCase {
    private final AccessControlPort accessControlPort;
    private final IdGenerationContext ids;

    @EventListener
    public void onResourceCreated(ResourceCreated e) {
        var userActor = (UserActor) e.actor();
        var domain = AccessControl.create(null, e.ref(), "owner", () -> ids.next(AccessControlId.class));

    }
}
