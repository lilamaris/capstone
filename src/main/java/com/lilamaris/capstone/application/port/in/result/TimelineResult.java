package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.Snapshot;
import com.lilamaris.capstone.domain.Timeline;
import lombok.Builder;

import java.util.List;

public class TimelineResult {
    @Builder
    public record Command(
            Timeline.Id id,
            String description,
            List<Snapshot.Id> snapshotIdList
    ) {
        public static Command from(Timeline domain) {
            return Command.builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshotIdList(domain.snapshotIdList())
                    .build();
        }
    }

    @Builder
    public record Query(
            Timeline.Id id,
            String description,
            List<SnapshotResult.Query> snapshots
    ) {
        public static Query from(Timeline domain, List<Snapshot> snapshots) {
            return Query.builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshots(snapshots.stream().map(SnapshotResult.Query::from).toList())
                    .build();
        }

        public static Query from(Timeline domain) {
            return Query.builder()
                    .id(domain.id())
                    .description(domain.description())
                    .build();
        }
    }
}
