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
            List<SnapshotResult.Command> snapshotList
    ) {
        public static Command from(Timeline domain) {
            return Command.builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshotList(domain.snapshotList().stream().map(SnapshotResult.Command::from).toList())
                    .build();
        }

        public static Command from(Timeline domain, List<Snapshot> snapshotList) {
            return Command.builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshotList(snapshotList.stream().map(SnapshotResult.Command::from).toList())
                    .build();
        }
    }

    @Builder
    public record Query(
            Timeline.Id id,
            String description,
            List<SnapshotResult.Query> snapshots
    ) {
        public static Query from(Timeline domain, List<Snapshot> snapshotList) {
            return Query.builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshots(snapshotList.stream().map(SnapshotResult.Query::from).toList())
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
