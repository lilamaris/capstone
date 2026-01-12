package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.timeline.application.result.TimelineResult;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.util.List;

public interface TimelineQueryUseCase {
    List<TimelineResult.Query> getAll();

    TimelineResult.Query getById(TimelineId id);
}
