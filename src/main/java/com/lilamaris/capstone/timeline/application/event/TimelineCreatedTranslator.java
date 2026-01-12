package com.lilamaris.capstone.timeline.application.event;

import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRoleResolver;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.event.boundary.EventTranslator;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceCreated;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceGranted;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import com.lilamaris.capstone.timeline.domain.event.TimelineCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TimelineCreatedTranslator implements EventTranslator<TimelineCreated> {
    private final DomainRoleResolver domainRoleResolver;

    @Override
    public List<DomainEvent> translate(TimelineCreated e) {
        var actor = ActorContext.get();
        var ownerRole = domainRoleResolver.ownerRoleOf(AggregateDomainType.TIMELINE);

        var created = new ResourceCreated(e.id().ref(), actor, e.occurredAt());
        var granted = new ResourceGranted(e.id().ref(), actor, actor, ownerRole.name(), e.occurredAt());

        return List.of(created, granted);
    }

    @Override
    public Class<TimelineCreated> supports() {
        return TimelineCreated.class;
    }
}
