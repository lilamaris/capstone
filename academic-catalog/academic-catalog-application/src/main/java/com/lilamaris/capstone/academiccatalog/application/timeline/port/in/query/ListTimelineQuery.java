package com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import org.jspecify.annotations.Nullable;

public record ListTimelineQuery(
        @Nullable String keyword,
        PageQuery pageQuery
) {
    public ListTimelineQuery {
        Preconditions.requireNonNull(pageQuery, "pageQuery");
    }

    public static ListTimelineQuery of(@Nullable String keyword, PageQuery pageQuery) {
        return new ListTimelineQuery(keyword, pageQuery);
    }

}
