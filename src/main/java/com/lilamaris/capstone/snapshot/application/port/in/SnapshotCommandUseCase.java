package com.lilamaris.capstone.snapshot.application.port.in;

import com.lilamaris.capstone.snapshot.application.result.SnapshotResult;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;

public interface SnapshotCommandUseCase {
    SnapshotResult.Command create(String title, String details);

    SnapshotResult.Command update(SnapshotId id, String title, String details);
}
