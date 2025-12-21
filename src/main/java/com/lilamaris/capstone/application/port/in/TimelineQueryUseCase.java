package com.lilamaris.capstone.application.port.in;

import com.lilamaris.capstone.application.port.in.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.application.port.in.result.SnapshotResult;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;

import java.util.List;

public interface TimelineQueryUseCase {
    List<TimelineResult.QueryCompressed> getAll();
    TimelineResult.QueryCompressed getCompressedById(TimelineId timelineId);
    List<SnapshotResult.Query> getSnapshot(SnapshotQueryCondition condition);
}
