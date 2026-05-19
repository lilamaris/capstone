package com.lilamaris.capstone.academiccatalog.domain.shared.time;

public enum RangeRelation {
    SAME,
    CONTAINS,
    CONTAINED_BY,
    BEFORE,
    AFTER,
    IMMEDIATELY_BEFORE,
    IMMEDIATELY_AFTER,
    OVERLAPS_AFTER,
    OVERLAPS_BEFORE;

    public boolean isSame() {
        return this == SAME;
    }

    public boolean isContains() {
        return this == CONTAINS;
    }

    public boolean isContainedBy() {
        return this == CONTAINED_BY;
    }

    public boolean isBefore() {
        return this == BEFORE;
    }

    public boolean isAfter() {
        return this == AFTER;
    }

    public boolean isImmediatelyBefore() {
        return this == IMMEDIATELY_BEFORE;
    }

    public boolean isImmediatelyAfter() {
        return this == IMMEDIATELY_AFTER;
    }

    public boolean isOverlapAfter() {
        return this == OVERLAPS_AFTER;
    }

    public boolean isOverlapBefore() {
        return this == OVERLAPS_BEFORE;
    }
}
