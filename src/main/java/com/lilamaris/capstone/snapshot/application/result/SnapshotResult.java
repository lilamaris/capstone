package com.lilamaris.capstone.snapshot.application.result;

import com.lilamaris.capstone.shared.application.result.DescriptionResult;
import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.snapshot.domain.SnapshotDelta;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotDeltaId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;

import java.util.List;

public class SnapshotResult {
    public record Command(
            SnapshotId id,
            SnapshotSlotId snapshotSlotId,
            List<SnapshotDeltaResult> snapshotDeltaList,
            DescriptionResult description
    ) {
        public static Command from(Snapshot domain) {
            var snapshotDeltaList = domain.getSnapshotDeltaList().stream().map(SnapshotDeltaResult::from).toList();
            return new Command(
                    domain.id(),
                    domain.getSnapshotSlotId(),
                    snapshotDeltaList,
                    DescriptionResult.from(domain)
            );
        }
    }

    public record Query(
            SnapshotId id,
            SnapshotSlotId snapshotSlotId,
            List<SnapshotDeltaResult> snapshotDeltaList,
            DescriptionResult description
    ) {
        public static Query from(Snapshot domain) {
            var snapshotDeltaList = domain.getSnapshotDeltaList().stream().map(SnapshotDeltaResult::from).toList();
            return new Query(
                    domain.id(),
                    domain.getSnapshotSlotId(),
                    snapshotDeltaList,
                    DescriptionResult.from(domain)
            );
        }
    }

    public record SnapshotDeltaResult(
            SnapshotDeltaId id,
            SnapshotId snapshotId,
            String jsonPatch
    ) {
        public static SnapshotDeltaResult from(SnapshotDelta domain) {
            return new SnapshotDeltaResult(
                    domain.id(),
                    domain.getSnapshotId(),
                    domain.getJsonPatch()
            );
        }
    }
}
