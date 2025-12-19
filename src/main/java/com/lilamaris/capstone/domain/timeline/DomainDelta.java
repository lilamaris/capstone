package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.application.util.JsonPatchEngine;
import com.lilamaris.capstone.domain.DomainId;
import com.lilamaris.capstone.domain.embed.Audit;
import com.lilamaris.capstone.domain.DomainType;
import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.event.DomainDeltaEventBase;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record DomainDelta (
        Id id,
        SnapshotLink.Id snapshotLinkId,
        String domainType,
        DomainId<?, ?> domainId,
        Patch patch,
        Audit audit
) {

    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "timeline.snapshot-link.domain-delta";
        }
    }

    public static class Id extends AbstractUUIDDomainId<Type> {
        public Id(UUID value) {
            super(value);
        }

        public Id() {
            super();
        }

        @Override
        public Type getDomainType() {
            return Type.INSTANCE;
        }
    }

    public interface Patch {
        <T extends DomainId<?, ?>> T convert(Class<T> clazz);
        <T extends DomainId<?, ?>> T apply(Class<T> clazz, T target);
        String getRaw();
    }

    public record JsonPatch (String jsonPatch) implements Patch {
        @Override
        public <T extends DomainId<?, ?>> T convert(Class<T> clazz) {
            return JsonPatchEngine.applyJsonPatch(jsonPatch, clazz);
        }

        @Override
        public <T extends DomainId<?, ?>> T apply(Class<T> clazz, T target) {
            return JsonPatchEngine.applyJsonPatch(jsonPatch, target, clazz);
        }

        @Override
        public String getRaw() {
            return jsonPatch;
        }
    }

    public DomainDelta {
        if (snapshotLinkId == null) throw new DomainIllegalArgumentException("Field 'snapshotLinkId' must not be null.");
        if (domainType == null) throw new DomainIllegalArgumentException("Field 'domainType' must not be null.");
        if (domainId == null) throw new DomainIllegalArgumentException("Field 'domainId' must not be null.");
        if (patch == null) throw new DomainIllegalArgumentException("Field 'patch' must not be null.");

        id = Optional.ofNullable(id).orElseGet(Id::new);
    }

    public static DomainDelta from(Id id, SnapshotLink.Id snapshotLinkId, String domainName, DomainId<?, ?> domainId, Patch patch, Audit audit) {
        return DomainDelta.getDefaultBuilder(snapshotLinkId, domainName, domainId, patch).id(id).audit(audit).build();
    }

    public static DomainDelta create(SnapshotLink.Id snapshotLinkId, String domainName, DomainId<?, ?> domainId, Patch patch) {
        return DomainDelta.getDefaultBuilder(snapshotLinkId, domainName, domainId, patch).build();
    }


    private static DomainDeltaBuilder getDefaultBuilder(SnapshotLink.Id snapshotLinkId, String domainType, DomainId<?, ?> domainId, Patch patch) {
        return builder().snapshotLinkId(snapshotLinkId).domainType(domainType).domainId(domainId).patch(patch);
    }
}
