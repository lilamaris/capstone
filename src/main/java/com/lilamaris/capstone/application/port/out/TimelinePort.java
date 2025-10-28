package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.Timeline;

import java.util.List;
import java.util.Optional;

public interface TimelinePort {
    Optional<Timeline> getById(Timeline.Id id);
    List<Timeline> getByIds(List<Timeline.Id> ids);

    Timeline save(Timeline domain);
    void delete(Timeline.Id id);
}
