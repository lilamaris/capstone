package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.config.ActorContext;
import com.lilamaris.capstone.application.config.access_control.privilege.timeline.TimelineAction;
import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.application.util.policy.DomainAuthorizer;
import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultDescriptionMetadata;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.id.IdGenerationContext;
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
    public TimelineResult.Command create(String title, String details) {
        var domain = Timeline.create(
                title, details, () -> ids.next(TimelineId.class)
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
    public TimelineResult.Command migrate(TimelineId id, LocalDateTime validAt, String details) {
        CanonicalActor actor = ActorContext.get();
        timelineAuthorizer.authorize(actor, id.ref(), TimelineAction.MIGRATE);

        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        timeline.migrate(
                UniversityClock.now(),
                UniversityClock.at(validAt),
                details,
                () -> ids.next(SnapshotId.class),
                () -> ids.next(SnapshotLinkId.class)
        );
        var saved = timelinePort.save(timeline);

        return TimelineResult.Command.from(saved);
    }

    @Override
    public TimelineResult.Command merge(TimelineId id, LocalDateTime validFrom, LocalDateTime validTo, String details) {
        CanonicalActor actor = ActorContext.get();
        timelineAuthorizer.authorize(actor, id.ref(), TimelineAction.MERGE);

        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        var validRange = Effective.create(
                UniversityClock.at(validFrom),
                UniversityClock.at(validTo)
        );
        timeline.mergeValidRange(
                UniversityClock.now(),
                validRange,
                details,
                () -> ids.next(SnapshotId.class),
                () -> ids.next(SnapshotLinkId.class)
        );
        var saved = timelinePort.save(timeline);

        return TimelineResult.Command.from(saved);
    }
}
