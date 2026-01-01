package com.lilamaris.capstone.timeline.application.service;

import com.lilamaris.capstone.snapshot.application.result.SnapshotResult;
import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.timeline.application.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.timeline.application.port.in.TimelineQueryUseCase;
import com.lilamaris.capstone.timeline.application.port.out.TimelinePort;
import com.lilamaris.capstone.timeline.application.result.TimelineResult;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return List.of();
    }
}
