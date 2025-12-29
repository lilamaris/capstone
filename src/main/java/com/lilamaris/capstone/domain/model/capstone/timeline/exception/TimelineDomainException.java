package com.lilamaris.capstone.domain.model.capstone.timeline.exception;

import com.lilamaris.capstone.domain.exception.DomainException;
import lombok.Getter;

@Getter
public class TimelineDomainException extends DomainException {
    private final TimelineErrorCode errorCode;

    public TimelineDomainException(TimelineErrorCode errorCode, String message) {
        super("TIMELINE_" + errorCode.name(), message);
        this.errorCode = errorCode;
    }
}
