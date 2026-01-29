package com.lilamaris.capstone.bitemporal.timeline.application.port.in;

import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;

public interface TimelineUpdater {
    TimelineEntry update(
            TimelineId id,
            String title,
            String details
    );
}
