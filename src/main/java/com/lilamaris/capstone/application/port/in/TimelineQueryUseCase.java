package com.lilamaris.capstone.application.port.in;

import com.lilamaris.capstone.application.port.in.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.application.port.in.result.SnapshotResult;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.domain.timeline.Timeline;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public interface TimelineQueryUseCase {
    List<TimelineResult.QueryCompressed> getAll();
    TimelineResult.QueryCompressed getCompressedById(Timeline.Id timelineId);
    List<SnapshotResult.Query> getSnapshot(SnapshotQueryCondition condition);
}
