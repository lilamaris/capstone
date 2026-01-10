package com.lilamaris.capstone.snapshot.application.port.out;

import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;

import java.util.List;
import java.util.Optional;

public interface SnapshotPort {
    Optional<Snapshot> getById(SnapshotId id);

    List<Snapshot> getByIds(List<SnapshotId> ids);

    Snapshot save(Snapshot snapshot);

    void delete(SnapshotId id);
}
