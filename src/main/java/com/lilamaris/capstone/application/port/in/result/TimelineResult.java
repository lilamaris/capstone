package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.model.capstone.snapshot.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.SnapshotSlot;
import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotSlotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;

import java.util.List;

public class TimelineResult {
    public record Command(
            TimelineId id,
            List<SnapshotSlotResult> snapshotSlotList,
            DescriptionResult description,
            AuditResult audit
    ) {
        public static Command from(Timeline domain) {
            var snapshotSlotList = domain.getSlotList().stream().map(SnapshotSlotResult::from).toList();
            return new Command(
                    domain.id(),
                    snapshotSlotList,
                    DescriptionResult.from(domain),
                    AuditResult.from(domain)
            );
        }
    }

    public record Query(
            TimelineId id,
            List<SnapshotSlotResult> snapshotSlotList,
            DescriptionResult description,
            AuditResult audit
    ) {
        public static Query from(Timeline domain) {
            var snapshotSlotList = domain.getSlotList().stream().map(SnapshotSlotResult::from).toList();
            return new Query(
                    domain.id(),
                    snapshotSlotList,
                    DescriptionResult.from(domain),
                    AuditResult.from(domain)
            );
        }
    }

    public record QueryCompressed(
            TimelineId id,
            Integer snapshotSlotNumber,
            DescriptionResult description,
            AuditResult audit
    ) {
        public static QueryCompressed from(Timeline domain) {
            return new QueryCompressed(
                    domain.id(),
                    domain.getSlotList().size(),
                    DescriptionResult.from(domain),
                    AuditResult.from(domain)
            );
        }
    }

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
}
