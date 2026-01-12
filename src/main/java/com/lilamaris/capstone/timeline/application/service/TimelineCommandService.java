package com.lilamaris.capstone.timeline.application.service;

import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAuthorizer;
import com.lilamaris.capstone.shared.application.util.UniversityClock;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDescriptionMetadata;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.timeline.application.policy.privilege.TimelineAction;
import com.lilamaris.capstone.timeline.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.timeline.application.port.out.TimelinePort;
import com.lilamaris.capstone.timeline.application.result.TimelineResult;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TimelineCommandService implements TimelineCommandUseCase {
    private final TimelinePort timelinePort;
    private final TimelineResourceUtil resourceUtil;

    private final IdGenerationDirectory ids;
    private final ResourceAuthorizer authorizer;

    @Override
    public TimelineResult.CommandCompressed create(String title, String details, LocalDateTime initialValidAt) {
        var domain = Timeline.create(
                ids.next(TimelineId.class),
                ids.next(SnapshotSlotId.class),
                title,
                details,
                UniversityClock.now(),
                UniversityClock.at(initialValidAt)
        );
        var created = timelinePort.save(domain);

        return TimelineResult.CommandCompressed.from(created);
    }

    @Override
    public TimelineResult.CommandCompressed update(TimelineId id, String title, String details) {
        CanonicalActor actor = ActorContext.get();
        authorizer.authorize(actor, id.ref(), TimelineAction.UPDATE_METADATA);

        var timeline = resourceUtil.getTimeline(id);

        timeline.updateDescription(new DefaultDescriptionMetadata(title, details));

        return TimelineResult.CommandCompressed.from(timeline);
    }

    @Override
    public TimelineResult.Command migrate(TimelineId id, LocalDateTime validAt) {
        CanonicalActor actor = ActorContext.get();
        authorizer.authorize(actor, id.ref(), TimelineAction.MIGRATE);

        var timeline = resourceUtil.getTimeline(id);

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
        authorizer.authorize(actor, id.ref(), TimelineAction.MERGE);

        var timeline = resourceUtil.getTimeline(id);

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
