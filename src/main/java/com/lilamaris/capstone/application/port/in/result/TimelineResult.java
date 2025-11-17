package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;
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
                    .snapshotList(domain.snapshotMap().values().stream().map(SnapshotResult.Command::from).toList())
                    .build();
        }
    }

    @Builder
    public record Query(
            Timeline.Id id,
            String description,
            List<SnapshotResult.Query> snapshotList
    ) {
        public static Query from(Timeline domain) {
            return Query.builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshotList(domain.snapshotMap().values().stream().map(SnapshotResult.Query::from).toList())
                    .build();
        }
    }

    @Builder
    public record QueryCompressed(
            Timeline.Id id,
            String description,
            Integer snapshotListNumber
    ) {
        public static QueryCompressed from(Timeline domain) {
            return QueryCompressed.builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshotListNumber(domain.snapshotMap().size())
                    .build();
        }
    }
}
