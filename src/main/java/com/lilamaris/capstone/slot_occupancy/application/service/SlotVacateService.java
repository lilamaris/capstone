package com.lilamaris.capstone.slot_occupancy.application.service;

import com.lilamaris.capstone.slot_occupancy.application.port.in.SlotVacater;
import com.lilamaris.capstone.slot_occupancy.application.port.out.SlotOccupancyStore;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlotVacateService implements SlotVacater {
    private final SlotOccupancyStore slotOccupancyStore;

    @Override
    public void vacate(SlotOccupancyId id) {
        slotOccupancyStore.deleteById(id);
    }
}
