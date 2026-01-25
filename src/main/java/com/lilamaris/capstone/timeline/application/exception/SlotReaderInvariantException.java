package com.lilamaris.capstone.timeline.application.exception;

import com.lilamaris.capstone.shared.application.exception.ApplicationInvariantException;

public class SlotReaderInvariantException extends ApplicationInvariantException {
    public SlotReaderInvariantException(String message) {
        super("TIMELINE_SLOT_INVARIANT", message);
    }
}
