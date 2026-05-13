package com.lilamaris.capstone.academiccatalog.application.timeline.port.out.criteria;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query.PageQuery;

public record PageCriteria(int size, int page) {
    public static PageCriteria of(int size, int page) {
        return new PageCriteria(size, page);
    }

    public static PageCriteria from(PageQuery query) {
        return new PageCriteria(query.size(), query.page());
    }
}
