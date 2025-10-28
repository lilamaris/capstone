package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.SnapshotResult;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.application.port.out.SnapshotPort;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.domain.EffectivePeriod;
import com.lilamaris.capstone.domain.Snapshot;
import com.lilamaris.capstone.domain.Timeline;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TimelineCommandService implements TimelineCommandUseCase {
    private final TimelinePort timelinePort;
    private final SnapshotPort snapshotPort;

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
    public List<SnapshotResult.Command> migrate(Timeline.Id id, LocalDateTime validAt, String description) {
        var txAt = EffectivePeriod.now();
        var timeline = timelinePort.getById(id).orElseThrow(EntityNotFoundException::new);

        List<SnapshotResult.Command> result;

        if (snapshotPort.isExistsInTimeline(id)) {
            Snapshot sourceSnapshot = snapshotPort.getByValidAt(validAt).getFirst();
            List<Snapshot> transition = timeline.migrate(sourceSnapshot, txAt, validAt, description);
            var saved = transition.stream().map(snapshotPort::save).toList();
            result = saved.stream().map(SnapshotResult.Command::from).toList();
        } else {
            Snapshot domain = Snapshot.initial(id, validAt, txAt, description);
            var saved = snapshotPort.save(domain);
            result = Stream.of(saved).map(SnapshotResult.Command::from).toList();
        }

        return result;
    }

    @Override
    public List<SnapshotResult.Command> merge(Timeline.Id id, List<Snapshot.Id> targetIds, String description) {
        var txAt = EffectivePeriod.now();
        var timeline = timelinePort.getById(id).orElseThrow(EntityNotFoundException::new);

        var targetSnapshots = snapshotPort.getByIds(targetIds);
        var transition = timeline.merge(targetSnapshots, txAt, description);
        var saved = transition.stream().map(snapshotPort::save).toList();

        return saved.stream().map(SnapshotResult.Command::from).toList();
    }

    @Override
    public List<SnapshotResult.Command> rollback(Timeline.Id id, LocalDateTime targetTxAt, String description) {
        return null;
    }
}
