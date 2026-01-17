package com.lilamaris.capstone.snapshot.application.event;

import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleResolver;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.event.boundary.EventTranslator;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceCreated;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceGranted;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import com.lilamaris.capstone.snapshot.domain.event.SnapshotCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SnapshotCreatedTranslator implements EventTranslator<SnapshotCreated> {
    private final DomainRoleResolver roleResolver;

    @Override
    public List<DomainEvent> translate(SnapshotCreated event) {
        var actor = ActorContext.get();
        var ownerRole = roleResolver.ownerRoleOf(AggregateDomainType.SNAPSHOT);

        var created = new ResourceCreated(event.id().ref(), actor, event.occurredAt());
        var granted = new ResourceGranted(event.id().ref(), actor, actor, ownerRole.name(), event.occurredAt());

        return List.of(created, granted);
    }

    @Override
    public Class<SnapshotCreated> supports() {
        return SnapshotCreated.class;
    }
}
