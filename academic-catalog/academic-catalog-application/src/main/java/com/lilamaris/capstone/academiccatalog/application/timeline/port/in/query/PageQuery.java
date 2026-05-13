package com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public record PageQuery(int page, int size) {
    public PageQuery {
        Preconditions.requireNonNegative(page, "page");
        Preconditions.requirePositive(size, "size");
    }

    public static PageQuery of(int page, int size) {
        return new PageQuery(page, size);
    }
}
