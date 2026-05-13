package com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.UUID;

public record GetTimelineQuery(UUID timelineId) {
    public GetTimelineQuery {
        Preconditions.requireNonNull(timelineId, "timelineId");
    }

    public static GetTimelineQuery of(UUID timelineId) {
        return new GetTimelineQuery(timelineId);
    }
}
