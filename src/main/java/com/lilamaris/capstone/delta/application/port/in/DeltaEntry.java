package com.lilamaris.capstone.delta.application.port.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.lilamaris.capstone.delta.domain.Delta;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchEngine;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import org.springframework.lang.Nullable;

import java.util.Optional;

public record DeltaEntry(
        DomainRef deltaRef,
        DomainRef resource,
        ExternalizableId slotId,
        @Nullable JsonNode state,
        @Nullable JsonPatch patch,
        AuditMetadata auditMetadata
) {
    public static DeltaEntry from(Delta delta, JsonPatchEngine jsonPatchEngine) {
        JsonNode state = delta.getState() != null
                ? jsonPatchEngine.parseNode(delta.getState())
                : null;
        JsonPatch patch = delta.getPatch() != null
                ? jsonPatchEngine.parsePatch(delta.getPatch())
                : null;
        return new DeltaEntry(
                delta.id().ref(),
                delta.getResource(),
                delta.getSlotId(),
                state,
                patch,
                delta.auditMetadata()
        );
    }
}
