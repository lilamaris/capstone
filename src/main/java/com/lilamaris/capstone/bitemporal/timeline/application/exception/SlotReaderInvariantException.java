package com.lilamaris.capstone.bitemporal.timeline.application.exception;

import com.lilamaris.capstone.shared.application.exception.ApplicationInvariantException;

public class SlotReaderInvariantException extends ApplicationInvariantException {
    public SlotReaderInvariantException(String message) {
        super("TIMELINE_SLOT_INVARIANT", message);
    }
}
