package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.application.port.in.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimelinePort {
    List<Timeline> getAll();
    List<Snapshot> getSnapshot(SnapshotQueryCondition condition);

    Optional<Timeline> getById(Timeline.Id id);
    List<Timeline> getByIds(List<Timeline.Id> ids);
    Timeline save(Timeline domain);
}
