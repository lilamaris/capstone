package com.lilamaris.capstone.domain.model.capstone.timeline.spec;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.common.id.IdSpec;

import java.util.UUID;

public class SnapshotIdSpec implements IdSpec<SnapshotId, UUID> {
    @Override
    public SnapshotId fromRaw(UUID raw) {
        return new SnapshotId(raw);
    }
}
