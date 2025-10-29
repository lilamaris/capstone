package com.lilamaris.capstone.application.port.in;

import com.lilamaris.capstone.application.port.in.result.SnapshotResult;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.domain.Timeline;

import java.time.LocalDateTime;
import java.util.List;

public interface TimelineCommandUseCase {
    TimelineResult.Command create(String description);
    TimelineResult.Command update(Timeline.Id id, String description);
    void delete(Timeline.Id id);

    TimelineResult.Command migrate(Timeline.Id id, LocalDateTime validAt, String description);
    TimelineResult.Command merge(Timeline.Id id, LocalDateTime validFrom, LocalDateTime validTo, String description);
    List<SnapshotResult.Command> rollback(Timeline.Id id, LocalDateTime targetTxAt, String description);
}
