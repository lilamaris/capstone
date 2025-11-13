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
        Integer versionNo,
        String description,
        Timeline.Id timelineId,
        Snapshot.Id baseSnapshotId,
        List<DomainDelta> deltaList
) implements BaseDomain<Snapshot.Id, Snapshot> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public Snapshot {
        id = Optional.ofNullable(id).orElseGet(Id::random);
        tx = Optional.ofNullable(tx).orElseThrow(() -> new IllegalArgumentException("'tx' must be set"));
        valid = Optional.ofNullable(valid).orElseThrow(() -> new IllegalArgumentException("'valid' must be set"));
        versionNo = Optional.ofNullable(versionNo).orElse(1);
        description = Optional.ofNullable(description).orElse("No user description.");
        timelineId = Optional.ofNullable(timelineId).orElseThrow(() -> new IllegalArgumentException("'timelineId' must be set"));
        deltaList = Optional.ofNullable(deltaList).orElseGet(ArrayList::new);
    }

    public static Snapshot create(Timeline.Id timelineId, Snapshot.Id baseSnapshotId, EffectiveConvertible tx, EffectiveConvertible valid, String description) {
        return Snapshot.builder()
                .timelineId(timelineId)
                .baseSnapshotId(baseSnapshotId)
                .tx(tx.convert())
                .valid(valid.convert())
                .description(description)
                .build();
    }

    public void validateOperation() {
        if (!tx.isOpen()) {
            throw new IllegalStateException("can only operated to snapshots with open tx");
        }
    }

    public Snapshot copyWithTx(Effective tx) {
        return toBuilder().tx(tx).versionNo(versionNo + 1).build();
    }

    public Snapshot copyWithValid(Effective valid) {
        return toBuilder().valid(valid).versionNo(versionNo + 1).build();
    }

    public Snapshot closeTxAt(LocalDateTime at) {
        return copyWithTx(tx.copyBeforeAt(at));
    }

    public Snapshot closeValidAt(LocalDateTime at) {
        return copyWithValid(valid.copyBeforeAt(at));
    }

    public List<DomainDelta> getDeltaOf(String domainName) {
        return deltaList.stream().filter(domainDelta -> domainDelta.domainName().equals(domainName)).toList();
    }

    public Snapshot addAllDelta(List<DomainDelta> domainDeltaList) {
        var updated = new ArrayList<>(deltaList);
        updated.addAll(domainDeltaList);
        return toBuilder().deltaList(updated).build();
    }

    public Snapshot addDelta(DomainDelta domainDelta) {
        var updated = new ArrayList<>(deltaList);
        updated.add(domainDelta);
        return toBuilder().deltaList(updated).build();
    }
}