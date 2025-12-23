package com.lilamaris.capstone.application.port.in;

import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;

import java.time.LocalDateTime;

public interface TimelineCommandUseCase {
    TimelineResult.Command create(String description);

    TimelineResult.Command update(TimelineId id, String description);

    TimelineResult.Command migrate(TimelineId id, LocalDateTime validAt, String description);

    TimelineResult.Command merge(TimelineId id, LocalDateTime validFrom, LocalDateTime validTo, String description);
//    TimelineResult.Command rollback(Timeline.Id id, LocalDateTime targetTxAt, String description);
}
