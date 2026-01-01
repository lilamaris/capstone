package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.timeline.application.result.TimelineResult;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.LocalDateTime;

public interface TimelineCommandUseCase {
    TimelineResult.CommandCompressed create(String title, String details, LocalDateTime initialValidAt);

    TimelineResult.CommandCompressed update(TimelineId id, String title, String details);

    TimelineResult.Command migrate(TimelineId id, LocalDateTime validAt);

    TimelineResult.Command merge(TimelineId id, LocalDateTime validFrom, LocalDateTime validTo);
}
