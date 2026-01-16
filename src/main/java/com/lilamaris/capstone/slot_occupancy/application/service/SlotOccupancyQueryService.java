package com.lilamaris.capstone.slot_occupancy.application.service;

import com.lilamaris.capstone.scenario.occupancy.application.port.out.SlotOccupancyEntry;
import com.lilamaris.capstone.scenario.occupancy.application.port.out.SlotOccupancyQuery;
import com.lilamaris.capstone.slot_occupancy.application.port.out.SlotOccupancyPort;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SlotOccupancyQueryService implements
        SlotOccupancyQuery {
    private final SlotOccupancyPort slotOccupancyPort;

    @Override
    public List<SlotOccupancyEntry> getOccupanciesBySlotIds(List<SlotId> ids) {
        return slotOccupancyPort.getBySlotIds(ids).stream()
                .map(SlotOccupancyEntry::from)
                .toList();
    }

    @Override
    public Optional<SlotOccupancyEntry> getOccupancyBySlotId(SlotId id) {
        return slotOccupancyPort.getBySlotId(id).map(SlotOccupancyEntry::from);
    }
}
