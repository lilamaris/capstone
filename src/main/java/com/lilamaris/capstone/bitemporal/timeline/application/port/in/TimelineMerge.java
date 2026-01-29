package com.lilamaris.capstone.bitemporal.timeline.application.port.in;

import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;

import java.time.Instant;

public interface TimelineMerge {
    TimelineEntry merge(
            TimelineId id,
            Instant mergeFrom,
            Instant mergeTo
    );
}
