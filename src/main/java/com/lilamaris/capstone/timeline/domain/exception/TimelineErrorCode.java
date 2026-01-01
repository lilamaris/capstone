package com.lilamaris.capstone.timeline.domain.exception;

public enum TimelineErrorCode {
    SNAPSHOT_LINK_INCONSISTENT,
    SNAPSHOT_LINK_CYCLE,
    EMPTY_SNAPSHOT,
    EMPTY_SLOT,
    NO_AVAILABLE_SNAPSHOT,
    NO_AVAILABLE_SLOT,
}
