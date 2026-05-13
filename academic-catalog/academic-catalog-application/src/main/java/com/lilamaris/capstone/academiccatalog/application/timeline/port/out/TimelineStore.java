package com.lilamaris.capstone.academiccatalog.application.timeline.port.out;

import com.lilamaris.capstone.academiccatalog.domain.timeline.Timeline;

public interface TimelineStore {
    Timeline save(Timeline timeline);

    void delete(Timeline timeline);
}
