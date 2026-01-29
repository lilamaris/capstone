package com.lilamaris.capstone.bitemporal.timeline.application.port.in;

import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;

import java.util.List;

public interface TimelineReader {
    List<TimelineEntry> getAll();

    TimelineEntry getById(TimelineId id);
}
