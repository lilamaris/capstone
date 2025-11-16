package com.lilamaris.capstone.domain.event;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.timeline.DomainDelta;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;
import lombok.Builder;

@Builder(toBuilder = true)
public record SnapshotDeltaEvent (
        Timeline.Id timelineId,
        Snapshot.Id snapshotId,
        String domainType,
        BaseDomain.Id<?> domainId,
        DomainDelta.Patch toPatch
) {
    public static SnapshotDeltaEvent fromBase(Timeline.Id timelineId, Snapshot.Id snapshotId, DomainDeltaEventBase delta) {
        return builder()
                .timelineId(timelineId)
                .snapshotId(snapshotId)
                .domainType(delta.domainType())
                .domainId(delta.domainId())
                .toPatch(delta.toPatch())
                .build();
    }
}
