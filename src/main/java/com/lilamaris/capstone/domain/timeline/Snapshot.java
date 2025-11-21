package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import lombok.Builder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record Snapshot(
        Id id,
        Effective tx,
        Effective valid,
        Timeline.Id timelineId,
        Integer versionNo,
        String description
) implements BaseDomain<Snapshot.Id, Snapshot> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public Snapshot {
        if (tx == null) throw new DomainIllegalArgumentException("Field 'tx' must not be null.");
        if (valid == null) throw new DomainIllegalArgumentException("Field 'valid' must not be null.");
        if (timelineId == null) throw new DomainIllegalArgumentException("Field 'timelineId' must not be null.");

        id = Optional.ofNullable(id).orElseGet(Id::random);
        versionNo = Optional.ofNullable(versionNo).orElse(1);
        description = Optional.ofNullable(description).orElse("No description");
    }

    public static Snapshot from(
            Id id,
            Effective tx,
            Effective valid,
            Timeline.Id timelineId,
            Integer versionNo,
            String description
    ) {
        return new Snapshot(id, tx, valid, timelineId, versionNo, description);
    }

    public static Snapshot create(
            Effective tx,
            Effective valid,
            Timeline.Id timelineId,
            String description
    ) {
        return new Snapshot(null, tx, valid, timelineId, 0, description);
    }

    public Snapshot closeTxAt(Instant at) {
        var closedTx = tx.closeAt(at);
        return copyWithTx(closedTx);
    }

    public Snapshot closeValidAt(Instant at) {
        var closedValid = tx.closeAt(at);
        return copyWithValid(closedValid);
    }

    private static SnapshotBuilder getDefaultBuilder(Effective tx, Effective valid, Timeline.Id timelineId) {
        return builder().tx(tx).valid(valid).timelineId(timelineId);
    }

    private Snapshot buildWithPolicy(SnapshotBuilder builder) {
        return builder.versionNo(versionNo + 1).build();
    }

    private Snapshot copyWithDescription(String description) {
        return buildWithPolicy(toBuilder().description(description));
    }

    private Snapshot copyWithTx(Effective tx) {
        return buildWithPolicy(toBuilder().tx(tx));
    }

    private Snapshot copyWithValid(Effective valid) {
        return buildWithPolicy(toBuilder().valid(valid));
    }
}