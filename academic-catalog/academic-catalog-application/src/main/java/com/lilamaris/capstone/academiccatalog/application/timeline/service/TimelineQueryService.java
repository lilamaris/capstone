package com.lilamaris.capstone.academiccatalog.application.timeline.service;

import com.lilamaris.capstone.academiccatalog.application.shared.exception.AcademicCatalogApplicationErrorCode;
import com.lilamaris.capstone.academiccatalog.application.shared.exception.AcademicCatalogApplicationException;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.GetTimelineUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.ListTimelineUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query.GetTimelineQuery;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query.ListTimelineQuery;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.PagedResult;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.TimelineResult;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineReader;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.criteria.TimelineSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineQueryService implements
        GetTimelineUseCase,
        ListTimelineUseCase {

    private final TimelineReader reader;

    @Override
    public TimelineResult get(GetTimelineQuery query) {
        return reader.findById(query.timelineId())
                .map(TimelineResult::from)
                .orElseThrow(() -> new AcademicCatalogApplicationException(AcademicCatalogApplicationErrorCode.TIMELINE_NOT_FOUND));
    }

    @Override
    public PagedResult<TimelineResult> list(ListTimelineQuery query) {
        var criteria = TimelineSearchCriteria.of(query.keyword(), query.pageQuery());
        return reader.findByCriteria(criteria).map(TimelineResult::from);
    }
}
