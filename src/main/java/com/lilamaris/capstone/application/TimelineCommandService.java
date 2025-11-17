package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.event.SnapshotDeltaEvent;
import com.lilamaris.capstone.domain.timeline.Timeline;
import jakarta.persistence.EntityNotFoundException;
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

    @Override
    public TimelineResult.Command create(String description) {
        var domain = Timeline.create(description);
        var created = timelinePort.save(domain);

        return TimelineResult.Command.from(created);
    }

    @Override
    public TimelineResult.Command update(Timeline.Id id, String description) {
        var timeline = timelinePort.getById(id).orElseThrow(EntityNotFoundException::new);
        var updated = timeline.copyWithDescription(description);
        var saved = timelinePort.save(updated);

        return TimelineResult.Command.from(saved);
    }

    @Override
    public TimelineResult.Command migrate(Timeline.Id id, LocalDateTime validAt, String description) {
        var txAt = Effective.now();
        var timeline = timelinePort.getById(id).orElseThrow(EntityNotFoundException::new);
        var updated = timeline.migrateSnapshot(txAt, validAt, description);
        var saved = timelinePort.save(updated);

        return TimelineResult.Command.from(saved);
    }

    @Override
    public TimelineResult.Command merge(Timeline.Id id, LocalDateTime validFrom, LocalDateTime validTo, String description) {
        var txAt = Effective.now();
        var timeline = timelinePort.getById(id).orElseThrow(EntityNotFoundException::new);
        var updated = timeline.mergeSnapshot(txAt, validFrom, validTo, description);
        var saved = timelinePort.save(updated);

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
        var timeline = timelinePort.getById(event.timelineId()).orElseThrow(EntityNotFoundException::new);
        var updated = timeline.updateDomainDelta(event.snapshotId(), event.domainType(), event.domainId(), event.toPatch());
        timelinePort.save(updated);
    }
}
