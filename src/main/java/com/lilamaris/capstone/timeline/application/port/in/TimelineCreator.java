package com.lilamaris.capstone.timeline.application.port.in;

import java.time.Instant;

public interface TimelineCreator {
    TimelineEntry create(
            String title,
            String details,
            Instant validAt
    );
}
