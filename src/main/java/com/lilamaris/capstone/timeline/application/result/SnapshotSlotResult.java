package com.lilamaris.capstone.timeline.application.result;

import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.SnapshotSlot;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

public record SnapshotSlotResult(
        SnapshotSlotId id,
        TimelineId timelineId,
        SnapshotSlotId parentSlotId,
        SnapshotId snapshotId,
        EffectiveResult tx,
        EffectiveResult valid
) {
    public static SnapshotSlotResult from(SnapshotSlot domain) {
        var txResult = EffectiveResult.from(domain.getTx());
        var validResult = EffectiveResult.from(domain.getValid());
        return new SnapshotSlotResult(
                domain.id(),
                domain.getTimelineId(),
                domain.getParentSlotId(),
                domain.getSnapshotId(),
                txResult,
                validResult
        );
    }
}