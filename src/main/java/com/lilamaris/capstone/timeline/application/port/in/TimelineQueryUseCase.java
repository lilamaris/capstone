package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.snapshot.application.result.SnapshotResult;
import com.lilamaris.capstone.timeline.application.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.timeline.application.result.TimelineResult;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.util.List;

public interface TimelineQueryUseCase {
    List<TimelineResult.QueryCompressed> getAll();

    TimelineResult.QueryCompressed getCompressedById(TimelineId timelineId);

    List<SnapshotResult.Query> getSnapshot(SnapshotQueryCondition condition);
}
