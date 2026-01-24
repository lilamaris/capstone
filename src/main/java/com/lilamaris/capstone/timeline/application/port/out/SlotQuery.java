package com.lilamaris.capstone.timeline.application.port.out;

import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.SlotClosure;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SlotQuery {
    Optional<Slot> getSlotById(SlotId slotId);

    List<Slot> getSlotByIds(List<SlotId> slotIds);

    List<Slot> getSlotsByTxTime(TimelineId id, Instant txAt);

    List<Slot> getSlotsByValidTime(TimelineId id, Instant validAt);

    List<SlotClosure> getClosureOf(SlotId descendantSlotId);

    Optional<SlotClosure> getParentOf(SlotId slotId);
}
