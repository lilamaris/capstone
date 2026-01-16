package com.lilamaris.capstone.timeline.application.port.out;

import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TimelinePort {
    List<Timeline> getAll();

    List<Timeline> getAllByIds(List<TimelineId> ids);

    Optional<Timeline> getById(TimelineId id);

    List<Slot> getSlotsByTxTime(TimelineId id, Instant txAt);

    Optional<Slot> getSlot(SlotId slotId);

    Timeline save(Timeline domain);
}
