package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.application.port.out.TransitionLogPort;
import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.timeline.Timeline;
import com.lilamaris.capstone.domain.transitionLog.TransitionLog;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TimelineCommandService implements TimelineCommandUseCase {
    private final TimelinePort timelinePort;
    private final TransitionLogPort transitionLogPort;

    @Override
    public TimelineResult.Command create(String description) {
        var domain = Timeline.initial(description);
        var created = timelinePort.save(domain);

        return TimelineResult.Command.from(created);
    }

    @Override
    public TimelineResult.Command update(Timeline.Id id, String description) {
        var domain = Timeline.from(id, description);
        var updated = timelinePort.save(domain);

        return TimelineResult.Command.from(updated);
    }

    @Override
    public void delete(Timeline.Id id) {
        timelinePort.delete(id);
    }

    @Override
    public TimelineResult.Command migrate(Timeline.Id id, LocalDateTime validAt, String description) {
        var txAt = Effective.now();
        var timeline = timelinePort.getById(id).orElseThrow(EntityNotFoundException::new);
        var preview = timeline.migratePreview(txAt, validAt, description);
        var apply = timeline.applyTransition(preview);
        var saved = timelinePort.save(apply);

        var logCtx = new TransitionLog.Context(saved.id().asString(), saved.getDomainName(), "Admin", txAt, description);
        var logs = TransitionLog.fromTransition(logCtx, List.of(preview));
        var logSaved = transitionLogPort.save(logs);

        return TimelineResult.Command.from(saved);
    }

    @Override
    public TimelineResult.Command merge(Timeline.Id id, LocalDateTime validFrom, LocalDateTime validTo, String description) {
        var txAt = Effective.now();
        var timeline = timelinePort.getById(id).orElseThrow(EntityNotFoundException::new);
        var preview = timeline.mergePreview(txAt, validFrom, validTo, description);
        var apply = timeline.applyTransition(preview);
        var saved = timelinePort.save(apply);

        var logCtx = new TransitionLog.Context(saved.id().asString(), saved.getDomainName(), "Admin", txAt, description);
        var logs = TransitionLog.fromTransition(logCtx, List.of(preview));
        var logSaved = transitionLogPort.save(logs);

        return TimelineResult.Command.from(saved);
    }

    @Override
    public TimelineResult.Command rollback(Timeline.Id id, LocalDateTime targetTxAt, String description) {
        var txAt = Effective.now();
        var timeline = timelinePort.getById(id).orElseThrow(EntityNotFoundException::new);
        var preview = timeline.rollback(txAt, targetTxAt);
        var apply = timeline.applyTransition(preview);
        var saved = timelinePort.save(apply);

        var logCtx = new TransitionLog.Context(saved.id().asString(), saved.getDomainName(), "Admin", txAt, description);
        var logs = TransitionLog.fromTransition(logCtx, List.of(preview));
        var logSaved = transitionLogPort.save(logs);

        return TimelineResult.Command.from(saved);
    }
}
