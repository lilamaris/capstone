package com.lilamaris.capstone.timeline.application.service;

import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.timeline.application.port.out.TimelinePort;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimelineResourceUtil {
    private final TimelinePort timelinePort;

    public Timeline getTimeline(TimelineId id) {
        return timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));
    }
}
