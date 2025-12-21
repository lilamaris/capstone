package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.Snapshot;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import lombok.Builder;

public class SnapshotResult {
    @Builder
    public record Command(
            SnapshotId id,
            EffectiveResult tx,
            EffectiveResult valid,
            Integer versionNo,
            String description,
            TimelineId timelineId
    ) {
        public static Command from(Snapshot domain) {
            return builder()
                    .id(domain.id())
                    .tx(EffectiveResult.from(domain.getTx()))
                    .valid(EffectiveResult.from(domain.getValid()))
                    .versionNo(domain.getVersionNo())
                    .description(domain.getDescription())
                    .timelineId(domain.getTimelineId())
                    .build();
        }
    }

    @Builder
    public record Query(
            SnapshotId id,
            EffectiveResult tx,
            EffectiveResult valid,
            Integer versionNo,
            String description,
            TimelineId timelineId
    ) {
        public static Query from(Snapshot domain) {
            return builder()
                    .id(domain.id())
                    .tx(EffectiveResult.from(domain.getTx()))
                    .valid(EffectiveResult.from(domain.getValid()))
                    .versionNo(domain.getVersionNo())
                    .description(domain.getDescription())
                    .timelineId(domain.getTimelineId())
                    .build();
        }
    }
}
