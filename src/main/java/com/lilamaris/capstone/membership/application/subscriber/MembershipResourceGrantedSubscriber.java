package com.lilamaris.capstone.membership.application.subscriber;

import com.lilamaris.capstone.membership.application.port.out.MembershipPort;
import com.lilamaris.capstone.membership.domain.Membership;
import com.lilamaris.capstone.membership.domain.MembershipStatus;
import com.lilamaris.capstone.membership.domain.id.MembershipId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceGranted;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MembershipResourceGrantedSubscriber {
    private final MembershipPort membershipPort;
    private final IdGenerationDirectory ids;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void onResourceGranted(ResourceGranted e) {
        Membership membership = membershipPort.getMembership(e.actor(), e.ref())
                .orElseGet(() -> {
                    var newMembership = Membership.create(
                            ids.next(MembershipId.class),
                            e.actor(),
                            e.ref()
                    );
                    membershipPort.save(newMembership);
                    return newMembership;
                });

        membership.setStatus(MembershipStatus.ACTIVE);
        membershipPort.save(membership);
    }
}
