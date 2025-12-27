package com.lilamaris.capstone.application.util.event.translator;

import com.lilamaris.capstone.application.config.ActorContext;
import com.lilamaris.capstone.domain.model.capstone.timeline.event.TimelineCreated;
import com.lilamaris.capstone.domain.model.capstone.timeline.policy.TimelineRole;
import com.lilamaris.capstone.domain.model.common.domain.event.DomainEvent;
import com.lilamaris.capstone.domain.model.common.domain.event.boundary.EventTranslator;
import com.lilamaris.capstone.domain.model.common.domain.event.canonical.ResourceCreated;
import com.lilamaris.capstone.domain.model.common.domain.event.canonical.ResourceGranted;
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
