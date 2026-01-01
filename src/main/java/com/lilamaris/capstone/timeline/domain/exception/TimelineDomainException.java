package com.lilamaris.capstone.timeline.domain.exception;

import com.lilamaris.capstone.shared.domain.exception.DomainException;
import lombok.Getter;

@Getter
public class TimelineDomainException extends DomainException {
    private final TimelineErrorCode errorCode;

    public TimelineDomainException(TimelineErrorCode errorCode, String message) {
        super("TIMELINE_" + errorCode.name(), message);
        this.errorCode = errorCode;
    }
}
