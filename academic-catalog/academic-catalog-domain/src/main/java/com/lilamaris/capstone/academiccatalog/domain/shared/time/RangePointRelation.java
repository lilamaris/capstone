package com.lilamaris.capstone.academiccatalog.domain.shared.time;

public enum RangePointRelation {
    BEFORE,
    SAME_AS_END,
    CONTAINS,
    SAME_AS_START,
    AFTER;

    public boolean isBefore() {
        return this == BEFORE;
    }

    public boolean isSameAsEnd() {
        return this == SAME_AS_END;
    }

    public boolean isContains() {
        return this == CONTAINS;
    }

    public boolean isSameAsStart() {
        return this == SAME_AS_START;
    }

    public boolean isAfter() {
        return this == AFTER;
    }
}