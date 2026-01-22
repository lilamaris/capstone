package com.lilamaris.capstone.scenario.occupancy.application.result;

import com.lilamaris.capstone.shared.application.result.EffectiveResult;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;
import org.springframework.lang.Nullable;

import java.util.Optional;


public class OccupancyResult {
    public record Query(
            EffectiveResult tx,
            EffectiveResult valid,
            @Nullable String snapshotId
    ) {
        public static Query from(EffectiveMetadata tx, EffectiveMetadata valid, @Nullable ExternalizableId externalSnapshotId) {
            var snapshotId = Optional.ofNullable(externalSnapshotId).map(ExternalizableId::asString).orElse(null);
            return new Query(
                    EffectiveResult.from(tx),
                    EffectiveResult.from(valid),
                    snapshotId
            );
        }
    }

    public record Command(
            EffectiveResult tx,
            EffectiveResult valid,
            String snapshotId
    ) {
        public static Command from(EffectiveMetadata tx, EffectiveMetadata valid, ExternalizableId snapshotId) {
            return new Command(
                    EffectiveResult.from(tx),
                    EffectiveResult.from(valid),
                    snapshotId.asString()
            );
        }
    }
}
