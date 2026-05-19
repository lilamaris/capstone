package com.lilamaris.capstone.academiccatalog.domain.shared.time;

public interface RangePointComparable<T> {
    boolean isBeforePoint(T other);

    boolean endAt(T other);

    boolean containsPoint(T other);

    boolean startAt(T other);

    boolean isAfterPoint(T other);

    default RangePointRelation relationToPoint(T other) {
        if (isBeforePoint(other)) return RangePointRelation.BEFORE;
        if (endAt(other)) return RangePointRelation.SAME_AS_END;
        if (startAt(other)) return RangePointRelation.SAME_AS_START;
        if (containsPoint(other)) return RangePointRelation.CONTAINS;
        return RangePointRelation.AFTER;
    }
}
