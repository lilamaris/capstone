package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.timeline.domain.id.TimelineId;

public interface TimelineUpdater {
    TimelineEntry update(
            TimelineId id,
            String title,
            String details
    );
}
