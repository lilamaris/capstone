package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public interface RangeComparable<
        SELF extends TemporalRange<BASE>,
        BASE extends Comparable<? super BASE>
        > extends TemporalRange<BASE> {
    default boolean isSame(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other.start()) == 0
                && end().compareTo(other.end()) == 0;
    }

    default boolean startsBefore(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other.start()) < 0;
    }

    default boolean endsAfter(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return end().compareTo(other.end()) > 0;
    }

    default boolean isBefore(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return end().compareTo(other.start()) < 0;
    }

    default boolean isBefore(BASE other) {
        Preconditions.requireNonNull(other, "other");

        return end().compareTo(other) < 0;
    }

    default boolean isImmediatelyBefore(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return end().compareTo(other.start()) == 0;
    }

    default boolean isEndsAt(BASE other) {
        Preconditions.requireNonNull(other, "other");

        return end().compareTo(other) == 0;
    }

    default boolean isAfter(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other.end()) > 0;
    }

    default boolean isAfter(BASE other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other) > 0;
    }

    default boolean isImmediatelyAfter(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other.end()) == 0;
    }

    default boolean isStartsAt(BASE other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other) == 0;
    }

    default boolean contains(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other.start()) <= 0
                && end().compareTo(other.end()) >= 0;
    }

    default boolean contains(BASE other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other) <= 0
                && end().compareTo(other) > 0;
    }
    default boolean containsBy(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other.start()) >= 0
                && end().compareTo(other.end()) <= 0;
    }

    /**
     * 교집합 확인
     * SAME, CONTAINS, CONTAINED_BY OVERLAPS 모두 포함
     */
    default boolean intersects(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return start().compareTo(other.end()) < 0
                && end().compareTo(other.start()) > 0;
    }

    /**
     * 부분 겹침만 확인
     */
    default boolean overlaps(SELF other) {
        Preconditions.requireNonNull(other, "other");

        return intersects(other)
                && !isSame(other)
                && !contains(other)
                && !containsBy(other);
    }

    default RangeRelation relationTo(SELF other) {
        Preconditions.requireNonNull(other, "other");

        if (isSame(other)) return RangeRelation.SAME;
        if (contains(other)) return RangeRelation.CONTAINS;
        if (containsBy(other)) return RangeRelation.CONTAINED_BY;
        if (isImmediatelyBefore(other)) return RangeRelation.IMMEDIATELY_BEFORE;
        if (isImmediatelyAfter(other)) return RangeRelation.IMMEDIATELY_AFTER;
        if (overlaps(other)) return RangeRelation.OVERLAPS;
        if (isBefore(other)) return RangeRelation.BEFORE;
        return RangeRelation.AFTER;
    }

    default RangePointRelation relationToPoint(BASE other) {
        Preconditions.requireNonNull(other, "other");

        if (isBefore(other)) return RangePointRelation.BEFORE;
        if (isStartsAt(other)) return RangePointRelation.SAME_AS_START;
        if (contains(other)) return RangePointRelation.CONTAINS;
        if (isEndsAt(other)) return RangePointRelation.SAME_AS_END;
        return RangePointRelation.AFTER;
    }
}
