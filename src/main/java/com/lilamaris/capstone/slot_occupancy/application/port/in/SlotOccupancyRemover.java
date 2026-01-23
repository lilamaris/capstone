package com.lilamaris.capstone.slot_occupancy.application.port.in;

import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;

public interface SlotOccupancyRemover {
    void remove(SlotOccupancyId id);
}
