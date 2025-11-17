package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.application.util.JsonPatchEngine;
import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.embed.Audit;
import com.lilamaris.capstone.domain.event.DomainDeltaEventBase;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record DomainDelta (
        Id id,
        SnapshotLink.Id snapshotLinkId,
        String domainType,
        BaseDomain.Id<?> domainId,
        Patch patch,
        Audit audit
) implements BaseDomain<DomainDelta.Id, DomainDelta> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public interface Patch {
        <T extends BaseDomain<?, ?>> T convert(Class<T> clazz);
        <T extends BaseDomain<?, ?>> T apply(Class<T> clazz, T target);
        String getRaw();
    }

    public record JsonPatch (String jsonPatch) implements Patch {
        @Override
        public <T extends BaseDomain<?, ?>> T convert(Class<T> clazz) {
            return JsonPatchEngine.applyJsonPatch(jsonPatch, clazz);
        }

        @Override
        public <T extends BaseDomain<?, ?>> T apply(Class<T> clazz, T target) {
            return JsonPatchEngine.applyJsonPatch(jsonPatch, target, clazz);
        }

        @Override
        public String getRaw() {
            return jsonPatch;
        }
    }

    public DomainDelta {
        id = Optional.ofNullable(id).orElseGet(Id::random);
        snapshotLinkId = Optional.ofNullable(snapshotLinkId).orElseThrow(() -> new IllegalArgumentException("'snapshotLinkId' of type SnapshotLink.Id cannot be null"));
        domainType = Optional.ofNullable(domainType).orElseThrow(() -> new IllegalArgumentException("'domainType' of type String cannot be null"));
        domainId = Optional.ofNullable(domainId).orElseThrow(() -> new IllegalArgumentException("'domainId' of type BaseDomain.Id<?> cannot be null"));
        patch = Optional.ofNullable(patch).orElseThrow(() -> new IllegalArgumentException("'patch' of type 'Patch' cannot be null"));
    }

    public static DomainDelta from(Id id, SnapshotLink.Id snapshotLinkId, String domainName, BaseDomain.Id<?> domainId, Patch patch, Audit audit) {
        return DomainDelta.getDefaultBuilder(snapshotLinkId, domainName, domainId, patch).id(id).audit(audit).build();
    }

    public static DomainDelta create(SnapshotLink.Id snapshotLinkId, String domainName, BaseDomain.Id<?> domainId, Patch patch) {
        return DomainDelta.getDefaultBuilder(snapshotLinkId, domainName, domainId, patch).build();
    }

    public static DomainDelta fromEvent(SnapshotLink.Id snapshotLinkId, DomainDeltaEventBase eventBase) {
        return builder()
                .snapshotLinkId(snapshotLinkId)
                .domainType(eventBase.domainType())
                .domainId(eventBase.domainId())
                .patch(eventBase.toPatch())
                .build();
    }

    private static DomainDeltaBuilder getDefaultBuilder(SnapshotLink.Id snapshotLinkId, String domainType, BaseDomain.Id<?> domainId, Patch patch) {
        return builder().snapshotLinkId(snapshotLinkId).domainType(domainType).domainId(domainId).patch(patch);
    }
}
