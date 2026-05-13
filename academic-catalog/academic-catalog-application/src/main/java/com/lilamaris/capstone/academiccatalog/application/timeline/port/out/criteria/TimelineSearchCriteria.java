package com.lilamaris.capstone.academiccatalog.application.timeline.port.out.criteria;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query.PageQuery;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import org.jspecify.annotations.Nullable;

public record TimelineSearchCriteria(
        @Nullable String keyword,
        PageCriteria pageCriteria
) {
    public TimelineSearchCriteria {
        Preconditions.requireNonNull(pageCriteria, "pageCriteria");
    }

    public static TimelineSearchCriteria of(@Nullable String keyword, PageCriteria pageCriteria) {
        return new TimelineSearchCriteria(keyword, pageCriteria);
    }

    public static TimelineSearchCriteria of(@Nullable String keyword, PageQuery pageQuery) {
        return of(keyword, PageCriteria.from(pageQuery));
    }
}
