package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.config.access_control.privilege.timeline.TimelineAction;
import com.lilamaris.capstone.application.context.ActorContext;
import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.policy.access_control.DomainAuthorizer;
import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotSlotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultDescriptionMetadata;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TimelineCommandService implements TimelineCommandUseCase {
    private final TimelinePort timelinePort;

    private final IdGenerationContext ids;
    private final DomainAuthorizer timelineAuthorizer;

    @Override
    public TimelineResult.Command create(String title, String details, LocalDateTime initialValidAt) {
        var domain = Timeline.create(
                ids.next(TimelineId.class),
                ids.next(SnapshotSlotId.class),
                title,
                details,
                UniversityClock.now(),
                UniversityClock.at(initialValidAt)
        );
        var created = timelinePort.save(domain);

        return TimelineResult.Command.from(created);
    }

    @Override
    public TimelineResult.Command update(TimelineId id, String title, String details) {
        CanonicalActor actor = ActorContext.get();
        timelineAuthorizer.authorize(actor, id.ref(), TimelineAction.UPDATE_METADATA);

        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        timeline.updateDescription(new DefaultDescriptionMetadata(title, details));

        return TimelineResult.Command.from(timeline);
    }

    @Override
    public TimelineResult.Command migrate(TimelineId id, LocalDateTime validAt) {
        CanonicalActor actor = ActorContext.get();
        timelineAuthorizer.authorize(actor, id.ref(), TimelineAction.MIGRATE);

        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        timeline.migrate(
                ids.next(SnapshotSlotId.class),
                UniversityClock.now(),
                UniversityClock.at(validAt)
        );
        var saved = timelinePort.save(timeline);

        return TimelineResult.Command.from(saved);
    }

    @Override
    public TimelineResult.Command merge(TimelineId id, LocalDateTime validFrom, LocalDateTime validTo) {
        CanonicalActor actor = ActorContext.get();
        timelineAuthorizer.authorize(actor, id.ref(), TimelineAction.MERGE);

        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        timeline.merge(
                ids.next(SnapshotSlotId.class),
                UniversityClock.now(),
                UniversityClock.at(validFrom),
                UniversityClock.at(validTo)
        );

        var saved = timelinePort.save(timeline);

        return TimelineResult.Command.from(saved);
    }
}
