package com.lilamaris.capstone.academiccatalog.application.timeline.port.in;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query.ListTimelineQuery;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.PagedResult;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.TimelineResult;

public interface ListTimelineUseCase {
    PagedResult<TimelineResult> list(ListTimelineQuery query);
}
