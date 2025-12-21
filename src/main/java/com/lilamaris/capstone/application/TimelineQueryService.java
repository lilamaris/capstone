package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.port.in.TimelineQueryUseCase;
import com.lilamaris.capstone.application.port.in.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.application.port.in.result.SnapshotResult;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.domain.model.capstone.timeline.Snapshot;
import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineQueryService implements TimelineQueryUseCase {
    private final TimelinePort timelinePort;

    @Override
    public List<TimelineResult.QueryCompressed> getAll() {
        return timelinePort.getAll().stream().map(TimelineResult.QueryCompressed::from).toList();
    }

    @Override
    public TimelineResult.QueryCompressed getCompressedById(TimelineId timelineId) {
        Timeline timeline = timelinePort.getById(timelineId).orElseThrow(EntityNotFoundException::new);
        return TimelineResult.QueryCompressed.from(timeline);
    }

    @Override
    public List<SnapshotResult.Query> getSnapshot(SnapshotQueryCondition condition) {
        List<Snapshot> snapshotList = timelinePort.getSnapshot(condition);
        return snapshotList.stream()
                .sorted(Comparator.comparing(snapshot -> snapshot.getTx().getFrom()))
                .map(SnapshotResult.Query::from)
                .toList();
    }
}
