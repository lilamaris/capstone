package com.lilamaris.capstone.snapshot.application.port.in;

import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;

public interface SnapshotUpdater {
    SnapshotEntry update(
            SnapshotId id,
            String title,
            String details
    );
}
