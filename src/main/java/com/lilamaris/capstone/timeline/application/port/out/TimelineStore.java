package com.lilamaris.capstone.timeline.application.port.out;

import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TimelineStore {
    List<Timeline> getAll();

    List<Timeline> getByIds(List<TimelineId> ids);

    Optional<Timeline> getById(TimelineId id);

    List<Slot> getSlotsByTxTime(TimelineId id, Instant txAt);

    List<Slot> getSlotsByValidTime(TimelineId id, Instant validAt);

    Optional<Slot> getSlotById(SlotId slotId);

    List<Slot> getSlotByIds(List<SlotId> slotIds);

    Timeline save(Timeline domain);
}
