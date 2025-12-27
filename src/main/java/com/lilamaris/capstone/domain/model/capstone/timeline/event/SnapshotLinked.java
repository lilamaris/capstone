package com.lilamaris.capstone.domain.model.capstone.timeline.event;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.common.event.aggregate.AggregateEvent;

import java.time.Instant;

public record SnapshotLinked(
        SnapshotLinkId id,
        SnapshotId ancestorSnapshotId,
        SnapshotId descendantSnapshotId,
        Instant occurredAt
) implements AggregateEvent {
}
