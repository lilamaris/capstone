package com.lilamaris.capstone.bitemporal.timeline.application.service;

import com.lilamaris.capstone.bitemporal.timeline.application.policy.privilege.TimelineAction;
import com.lilamaris.capstone.bitemporal.timeline.application.port.in.*;
import com.lilamaris.capstone.bitemporal.timeline.application.port.out.TimelineStore;
import com.lilamaris.capstone.bitemporal.timeline.domain.Timeline;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotClosureId;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotId;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAuthorizer;
import com.lilamaris.capstone.shared.application.util.UniversityClock;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDescriptionMetadata;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineService implements
        TimelineCreator,
        TimelineUpdater,
        TimelineMigration,
        TimelineMerge,
        TimelineReader {
    private final TimelineStore timelineStore;
    private final IdGenerationDirectory ids;
    private final ResourceAuthorizer authorizer;

    @Override
    public List<TimelineEntry> getAll() {
        return timelineStore.getAll().stream().map(TimelineEntry::from).toList();
    }

    @Override
    public TimelineEntry getById(TimelineId id) {
        var timeline = timelineStore.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with snapshotRef '%s' not found.", id)
        ));
        return TimelineEntry.from(timeline);
    }

    @Override
    public TimelineEntry create(String title, String details, Instant validAt) {
        var domain = Timeline.create(
                ids.next(TimelineId.class),
                ids.next(SlotId.class),
                ids.next(SlotClosureId.class),
                title,
                details,
                UniversityClock.now(),
                validAt
        );

        var saved = timelineStore.save(domain);

        return TimelineEntry.from(saved);
    }

    @Override
    public TimelineEntry update(TimelineId id, String title, String details) {
        CanonicalActor actor = ActorContext.get();
        authorizer.authorize(actor, id.ref(), TimelineAction.UPDATE_METADATA);

        var timeline = timelineStore.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with snapshotRef '%s' not found.", id)
        ));

        timeline.updateDescription(new DefaultDescriptionMetadata(title, details));

        return TimelineEntry.from(timeline);
    }

    @Override
    public TimelineEntry migrate(TimelineId id, Instant migrateAt) {
        CanonicalActor actor = ActorContext.get();
        authorizer.authorize(actor, id.ref(), TimelineAction.MIGRATE);

        var timeline = timelineStore.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with snapshotRef '%s' not found.", id)
        ));

        timeline.migrate(
                ids.next(SlotId.class),
                ids.next(SlotClosureId.class),
                UniversityClock.now(),
                migrateAt
        );

        var saved = timelineStore.save(timeline);

        return TimelineEntry.from(saved);
    }

    @Override
    public TimelineEntry merge(TimelineId id, Instant mergeFrom, Instant mergeTo) {
        CanonicalActor actor = ActorContext.get();
        authorizer.authorize(actor, id.ref(), TimelineAction.MERGE);

        var timeline = timelineStore.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with snapshotRef '%s' not found.", id)
        ));

        timeline.merge(
                ids.next(SlotId.class),
                ids.next(SlotClosureId.class),
                UniversityClock.now(),
                mergeFrom,
                mergeTo
        );

        var saved = timelineStore.save(timeline);

        return TimelineEntry.from(saved);
    }
}
