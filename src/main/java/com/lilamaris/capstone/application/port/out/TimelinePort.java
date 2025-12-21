package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.application.port.in.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.domain.model.capstone.timeline.Snapshot;
import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;

import java.util.List;
import java.util.Optional;

public interface TimelinePort {
    List<Timeline> getAll();
    List<Snapshot> getSnapshot(SnapshotQueryCondition condition);

    Optional<Timeline> getById(TimelineId id);
    List<Timeline> getByIds(List<TimelineId> ids);
    Timeline save(Timeline domain);
}
