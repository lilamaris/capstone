package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.Snapshot;
import com.lilamaris.capstone.domain.Timeline;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SnapshotPort {
    boolean isExistsInTimeline(Timeline.Id timelineId);

    Optional<Snapshot> getById(Snapshot.Id id);
    List<Snapshot> getByIds(List<Snapshot.Id> ids);

    List<Snapshot> getByValidAt(LocalDateTime validAt);
    List<Snapshot> getByTxAt(LocalDateTime txAt);

    Snapshot save(Snapshot domain);
    List<Snapshot> save(List<Snapshot> domains);
    void delete(Snapshot.Id id);
}
