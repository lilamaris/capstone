package com.lilamaris.capstone.timeline.application.port.out;

import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.util.List;
import java.util.Optional;

public interface TimelineStore {
    List<Timeline> getAll();

    List<Timeline> getByIds(List<TimelineId> ids);

    Optional<Timeline> getById(TimelineId id);

    Timeline save(Timeline domain);
}
