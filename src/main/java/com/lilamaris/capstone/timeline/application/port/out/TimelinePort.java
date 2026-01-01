package com.lilamaris.capstone.timeline.application.port.out;

import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.timeline.application.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.util.List;
import java.util.Optional;

public interface TimelinePort {
    List<Timeline> getAll();

    List<Snapshot> getSnapshot(SnapshotQueryCondition condition);

    Optional<Timeline> getById(TimelineId id);

    List<Timeline> getByIds(List<TimelineId> ids);

    Timeline save(Timeline domain);
}
