package com.lilamaris.capstone.academiccatalog.application.timeline.port.in;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query.GetTimelineQuery;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.TimelineResult;

public interface GetTimelineUseCase {
    TimelineResult get(GetTimelineQuery query);
}
