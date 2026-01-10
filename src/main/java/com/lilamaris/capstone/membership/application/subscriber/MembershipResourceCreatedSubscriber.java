package com.lilamaris.capstone.membership.application.subscriber;

import com.lilamaris.capstone.membership.application.port.out.MembershipPort;
import com.lilamaris.capstone.membership.domain.Membership;
import com.lilamaris.capstone.membership.domain.id.MembershipId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MembershipResourceCreatedSubscriber {
    private final MembershipPort membershipPort;
    private final IdGenerationDirectory ids;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void onResourceCreated(ResourceCreated e) {
        var membership = Membership.create(
                ids.next(MembershipId.class),
                e.actor(),
                e.ref()
        );
        membershipPort.save(membership);
    }
}
