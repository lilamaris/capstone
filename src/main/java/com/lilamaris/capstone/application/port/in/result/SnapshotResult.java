package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;
import lombok.Builder;

public class SnapshotResult {
    @Builder
    public record Command(
            Snapshot.Id id,
            Effective tx,
            Effective valid,
            Integer versionNo,
            String description,
            Timeline.Id timelineId
    ) {
        public static Command from(Snapshot domain) {
            return builder()
                    .id(domain.id())
                    .tx(domain.tx())
                    .valid(domain.valid())
                    .versionNo(domain.versionNo())
                    .description(domain.description())
                    .timelineId(domain.timelineId())
                    .build();
        }
    }

    @Builder
    public record Query(
            Snapshot.Id id,
            Effective tx,
            Effective valid,
            Integer versionNo,
            String description,
            Timeline.Id timelineId
    ) {
        public static Query from(Snapshot domain) {
            return builder()
                    .id(domain.id())
                    .tx(domain.tx())
                    .valid(domain.valid())
                    .versionNo(domain.versionNo())
                    .description(domain.description())
                    .timelineId(domain.timelineId())
                    .build();
        }
    }
}
