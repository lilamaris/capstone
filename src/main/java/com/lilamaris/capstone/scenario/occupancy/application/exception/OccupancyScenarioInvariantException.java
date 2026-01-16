package com.lilamaris.capstone.scenario.occupancy.application.exception;

import com.lilamaris.capstone.shared.application.exception.ApplicationInvariantException;

public class OccupancyScenarioInvariantException extends ApplicationInvariantException {
    public OccupancyScenarioInvariantException(String message) {
        super("OCCUPANCY_EXISTS", message);
    }
}
