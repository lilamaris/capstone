package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;

public interface TimelineMerge {
    TimelineEntry merge(
            TimelineId id,
            Instant mergeFrom,
            Instant mergeTo
    );
}
