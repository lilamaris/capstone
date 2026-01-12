package com.lilamaris.capstone.timeline.application.port.out;

import com.lilamaris.capstone.timeline.domain.SnapshotSlot;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TimelinePort {
    List<Timeline> getAll();

    List<Timeline> getAllByIds(List<TimelineId> ids);

    Optional<Timeline> getById(TimelineId id);

    List<SnapshotSlot> getSlotsByTxTime(TimelineId id, Instant txAt);

    Optional<SnapshotSlot> getSlot(SnapshotSlotId snapshotSlotId);

    Timeline save(Timeline domain);
}
