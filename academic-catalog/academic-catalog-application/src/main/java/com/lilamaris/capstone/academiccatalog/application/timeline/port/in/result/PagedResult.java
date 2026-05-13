package com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.List;
import java.util.function.Function;

public record PagedResult<T>(
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        List<T> items
) {
    public PagedResult {
        Preconditions.requireNonNegative(pageNumber, "pageNumber");
        Preconditions.requirePositive(pageSize, "pageSize");
        Preconditions.requireNonNegative(totalElements, "totalElements");
        Preconditions.requireNonNegative(totalPages, "totalPages");
        items = List.copyOf(Preconditions.requireNonNull(items, "items"));
    }

    public static <T> PagedResult<T> of(int pageNumber, int pageSize, long totalElements, int totalPages, List<T> items) {
        return new PagedResult<>(pageNumber, pageSize, totalElements, totalPages, items);
    }

    public <R> PagedResult<R> map(Function<T, R> mapper) {
        return of(pageNumber, pageSize, totalElements, totalPages, items.stream().map(mapper).toList());
    }
}
