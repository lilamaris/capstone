package com.lilamaris.capstone.timeline.application.event;

import com.lilamaris.capstone.shared.application.config.access_control.privilege.timeline.TimelineRole;
import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.event.boundary.EventTranslator;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceCreated;
import com.lilamaris.capstone.shared.domain.event.canonical.ResourceGranted;
import com.lilamaris.capstone.timeline.domain.event.TimelineCreated;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimelineCreatedTranslator implements EventTranslator<TimelineCreated> {
    @Override
    public List<DomainEvent> translate(TimelineCreated e) {
        var actor = ActorContext.get();

        var created = new ResourceCreated(e.id().ref(), actor, e.occurredAt());
        var granted = new ResourceGranted(e.id().ref(), actor, actor, TimelineRole.MAINTAINER.name(), e.occurredAt());

        return List.of(created, granted);
    }

    @Override
    public Class<TimelineCreated> supports() {
        return TimelineCreated.class;
    }
}
