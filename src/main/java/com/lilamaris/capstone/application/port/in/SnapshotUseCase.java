package com.lilamaris.capstone.application.port.in;

import com.lilamaris.capstone.application.port.in.result.SnapshotResult;
import com.lilamaris.capstone.domain.Snapshot;

public interface SnapshotUseCase {
    SnapshotResult.Command update(Snapshot.Id id, String description);
}
