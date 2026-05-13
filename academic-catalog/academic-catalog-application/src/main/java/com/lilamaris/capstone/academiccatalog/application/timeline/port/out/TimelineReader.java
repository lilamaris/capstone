package com.lilamaris.capstone.academiccatalog.application.timeline.port.out;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.PagedResult;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.criteria.TimelineSearchCriteria;
import com.lilamaris.capstone.academiccatalog.domain.timeline.Timeline;

import java.util.Optional;
import java.util.UUID;

public interface TimelineReader {
    boolean existsById(UUID id);

    Optional<Timeline> findById(UUID id);

    PagedResult<Timeline> findByCriteria(TimelineSearchCriteria criteria);
}
