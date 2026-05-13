package com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result;

import com.lilamaris.capstone.academiccatalog.domain.timeline.Timeline;

import java.time.Instant;
import java.util.UUID;

public record TimelineResult(
        UUID timelineId,
        String title,
        String description,
        Instant createdAt
) {
    public static TimelineResult from(Timeline timeline) {
        return new TimelineResult(timeline.getId(), timeline.getTitle(), timeline.getDescription(), timeline.getCreatedAt());
    }
}
