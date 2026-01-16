package com.lilamaris.capstone.scenario.slot_occupancy.application.exception;

import com.lilamaris.capstone.shared.application.exception.ApplicationInvariantException;

public class SlotOccupancyScenarioInvariantException extends ApplicationInvariantException {
    public SlotOccupancyScenarioInvariantException(String message) {
        super("OCCUPANCY_EXISTS", message);
    }
}
