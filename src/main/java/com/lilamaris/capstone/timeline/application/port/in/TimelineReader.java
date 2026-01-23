package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.util.List;

public interface TimelineReader {
    List<TimelineEntry> getAll();

    TimelineEntry getById(TimelineId id);
}
