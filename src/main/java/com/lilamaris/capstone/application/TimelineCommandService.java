package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.event.SnapshotDeltaEvent;
import com.lilamaris.capstone.domain.model.capstone.timeline.TimelineFactory;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TimelineCommandService implements TimelineCommandUseCase {
    private final TimelinePort timelinePort;

    private final TimelineFactory timelineFactory;

    @Override
    public TimelineResult.Command create(String description) {
        var domain = timelineFactory.create(description);
        var created = timelinePort.save(domain);

        return TimelineResult.Command.from(created);
    }

    @Override
    public TimelineResult.Command update(TimelineId id, String description) {
        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        return TimelineResult.Command.from(timeline);
    }

    @Override
    public TimelineResult.Command migrate(TimelineId id, LocalDateTime validAt, String description) {
        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        timeline.migrate(UniversityClock.now(), UniversityClock.at(validAt), description);
        var saved = timelinePort.save(timeline);

        return TimelineResult.Command.from(saved);
    }

    @Override
    public TimelineResult.Command merge(TimelineId id, LocalDateTime validFrom, LocalDateTime validTo, String description) {
        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        var validRange = Effective.create(
                UniversityClock.at(validFrom),
                UniversityClock.at(validTo)
        );
        timeline.mergeValidRange(UniversityClock.now(), validRange, description);
        var saved = timelinePort.save(timeline);

        return TimelineResult.Command.from(saved);
    }

//    @Override
//    public TimelineResult.Command rollback(Timeline.Id id, LocalDateTime targetTxAt, String description) {
//        var txAt = Effective.now();
//        var timeline = timelinePort.getById(id).orElseThrow(EntityNotFoundException::new);
//        var updated = timeline.rollbackSnapshot(txAt, targetTxAt);
//        var apply = timeline.applyTransition(preview);
//        var saved = timelinePort.save(apply);
//
//        return TimelineResult.Command.from(saved);
//    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSnapshotDeltaEvent(SnapshotDeltaEvent event) {
        // TODO: We expect all fields of SnapshotDeltaEvent to be non-null. The structure must be changed.
//        var id = event.timelineId();
//
//        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
//                String.format("Timeline with id '%s' not found.", id.getValue())
//        ));
//
//        var updated = timeline.updateDomainDelta(event.snapshotId(), event.domainType(), event.domainId(), event.toPatch());
//        timelinePort.save(updated);
    }
}
