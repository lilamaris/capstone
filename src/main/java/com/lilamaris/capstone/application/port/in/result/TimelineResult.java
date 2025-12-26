package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import lombok.Builder;

import java.util.List;

public class TimelineResult {
    @Builder
    public record Command(
            TimelineId id,
            DescriptionResult description,
            List<SnapshotResult.Command> snapshotList,
            AuditResult audit
    ) {
        public static Command from(Timeline domain) {
            return builder()
                    .id(domain.id())
                    .description(DescriptionResult.from(domain.getDescriptionMetadata()))
                    .snapshotList(domain.getSnapshotList().stream().map(SnapshotResult.Command::from).toList())
                    .audit(AuditResult.from(domain.getAudit()))
                    .build();
        }
    }

    @Builder
    public record Query(
            TimelineId id,
            DescriptionResult description,
            List<SnapshotResult.Query> snapshotList,
            AuditResult audit
    ) {
        public static Query from(Timeline domain) {
            return builder()
                    .id(domain.id())
                    .description(DescriptionResult.from(domain.getDescriptionMetadata()))
                    .snapshotList(domain.getSnapshotList().stream().map(SnapshotResult.Query::from).toList())
                    .audit(AuditResult.from(domain.getAudit()))
                    .build();
        }
    }

    @Builder
    public record QueryCompressed(
            TimelineId id,
            DescriptionResult description,
            Integer snapshotListNumber,
            AuditResult audit
    ) {
        public static QueryCompressed from(Timeline domain) {
            return builder()
                    .id(domain.id())
                    .description(DescriptionResult.from(domain.getDescriptionMetadata()))
                    .snapshotListNumber(domain.getSnapshotList().size())
                    .audit(AuditResult.from(domain.getAudit()))
                    .build();
        }
    }
}
