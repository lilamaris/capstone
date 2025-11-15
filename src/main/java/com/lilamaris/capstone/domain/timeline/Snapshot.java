package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.embed.EffectiveConvertible;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.*;

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
        id = Optional.ofNullable(id).orElseGet(Id::random);

        tx = Optional.ofNullable(tx).orElseThrow(() -> new IllegalArgumentException("'tx' of type Effective cannot be null"));
        valid = Optional.ofNullable(valid).orElseThrow(() -> new IllegalArgumentException("'valid' of type Effective cannot be null"));
        timelineId = Optional.ofNullable(timelineId).orElseThrow(() -> new IllegalArgumentException("'timelineId' of type Timeline.Id cannot be null"));

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
        return getDefaultBuilder(tx, valid, timelineId)
                .id(id)
                .versionNo(versionNo)
                .description(description)
                .build();
    }

    public static Snapshot create(
            EffectiveConvertible tx,
            EffectiveConvertible valid,
            Timeline.Id timelineId,
            String description
    ) {
        return getDefaultBuilder(tx.convert(), valid.convert(), timelineId)
                .description(description)
                .build();
    }

    public Snapshot closeTxAt(LocalDateTime at) {
        var updated = toBuilder().tx(tx.copyBeforeAt(at));
        return buildWithPolicy(updated);
    }

    public Snapshot closeValidAt(LocalDateTime at) {
        var updated = toBuilder().valid(valid.copyBeforeAt(at));
        return buildWithPolicy(updated);
    }

    public Snapshot copyWithTimelineId(Timeline.Id timelineId) {
        return toBuilder().timelineId(timelineId).build();
    }

//    public List<DomainDelta> getDeltaOf(String domainType) {
//        return domainDeltaList.stream().filter(domainDelta -> domainDelta.domainType().equals(domainType)).toList();
//    }

    private Snapshot buildWithPolicy(SnapshotBuilder builder) {
        return builder.versionNo(versionNo + 1).build();
    }

    private static SnapshotBuilder getDefaultBuilder(Effective tx, Effective valid, Timeline.Id timelineId) {
        return builder().tx(tx).valid(valid).timelineId(timelineId);
    }
}