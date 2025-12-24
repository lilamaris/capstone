package com.lilamaris.capstone.domain.model.capstone.timeline.spec;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.common.id.IdSpec;

import java.util.UUID;

public class SnapshotLinkIdSpec implements IdSpec<SnapshotLinkId, UUID> {
    @Override
    public SnapshotLinkId fromRaw(UUID raw) {
        return new SnapshotLinkId(raw);
    }
}
