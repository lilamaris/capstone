package com.lilamaris.capstone.slot_occupancy.application.exception;

import com.lilamaris.capstone.shared.application.exception.ApplicationInvariantException;

public class AlreadyOccupiedException extends ApplicationInvariantException {
    public AlreadyOccupiedException(String message) {
        super("ALREADY_OCCUPIED", message);
    }
}
