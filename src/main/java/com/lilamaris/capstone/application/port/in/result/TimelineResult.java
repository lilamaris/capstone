package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.timeline.Timeline;
import lombok.Builder;

import java.util.List;

public class TimelineResult {
    @Builder
    public record Command(
            Timeline.Id id,
            String description,
            List<SnapshotResult.Command> snapshotList,
            AuditResult audit
    ) {
        public static Command from(Timeline domain) {
            return builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshotList(domain.snapshotMap().values().stream().map(SnapshotResult.Command::from).toList())
                    .audit(AuditResult.from(domain.audit()))
                    .build();
        }
    }

    @Builder
    public record Query(
            Timeline.Id id,
            String description,
            List<SnapshotResult.Query> snapshotList,
            AuditResult audit
    ) {
        public static Query from(Timeline domain) {
            return builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshotList(domain.snapshotMap().values().stream().map(SnapshotResult.Query::from).toList())
                    .audit(AuditResult.from(domain.audit()))
                    .build();
        }
    }

    @Builder
    public record QueryCompressed(
            Timeline.Id id,
            String description,
            Integer snapshotListNumber,
            AuditResult audit
    ) {
        public static QueryCompressed from(Timeline domain) {
            return builder()
                    .id(domain.id())
                    .description(domain.description())
                    .snapshotListNumber(domain.snapshotMap().size())
                    .audit(AuditResult.from(domain.audit()))
                    .build();
        }
    }
}
