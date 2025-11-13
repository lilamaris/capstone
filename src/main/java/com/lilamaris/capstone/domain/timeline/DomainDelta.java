package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.application.util.JsonPatchEngine;
import com.lilamaris.capstone.domain.BaseDomain;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record DomainDelta (
        Id id,
        Snapshot.Id snapshotId,
        String domainName,
        BaseDomain.Id<?> domainId,
        Patch patch
) implements BaseDomain<DomainDelta.Id, DomainDelta> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public interface Patch {
        boolean isCreated();
        <T extends BaseDomain<?, ?>> T convert(Class<T> clazz);
        <T extends BaseDomain<?, ?>> T apply(Class<T> clazz, T target);
        String getRaw();
    }

    public record JsonPatch (String jsonPatch) implements Patch {
        @Override
        public boolean isCreated() {
            return jsonPatch.contains("\"op\"") && jsonPatch.contains("\"add\"");
        }

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
}
