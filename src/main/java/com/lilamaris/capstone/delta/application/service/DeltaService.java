package com.lilamaris.capstone.delta.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.lilamaris.capstone.delta.application.port.in.*;
import com.lilamaris.capstone.delta.application.port.out.DeltaStore;
import com.lilamaris.capstone.delta.domain.Delta;
import com.lilamaris.capstone.delta.domain.id.DeltaId;
import com.lilamaris.capstone.shared.application.exception.ResourceAlreadyExistsException;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchEngine;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.id.CanonicalExternalId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeltaService implements
        DeltaExistenceChecker,
        DeltaReader,
        DeltaIssuer,
        DeltaRevoker {
    private final DeltaStore deltaStore;
    private final IdGenerationDirectory ids;
    private final JsonPatchEngine jsonPatchEngine;

    @Override
    public boolean isExist(DomainRef resourceRef, ExternalizableId slotId) {
        return deltaStore.isExists(resourceRef, slotId);
    }

    @Override
    public DeltaEntry issue(
            DomainRef resource,
            ExternalizableId slotId,
            @Nullable JsonNode state,
            @Nullable JsonPatch patch
    ) {
        var exists = deltaStore.isExists(resource, slotId);
        if (exists) throw new ResourceAlreadyExistsException(String.format(
                "Delta already exists with resource type '%s' and id '%s' in slot id '%s'.",
                resource.type().name(),
                resource.id().asString(),
                slotId.asString()
        ));

        var delta = Delta.create(
                jsonPatchEngine,
                ids.next(DeltaId.class),
                slotId,
                resource,
                state,
                patch);

        var saved = deltaStore.save(delta);

        return DeltaEntry.from(saved, jsonPatchEngine);
    }

    @Override
    public void revoke(DomainRef resource, ExternalizableId slotId) {
        deltaStore.delete(resource, slotId);
    }

    @Override
    public Map<CanonicalExternalId, List<DeltaEntry>> getDelta(DeltaReadOption option) {
        return deltaStore.getDelta(option).stream().collect(Collectors.groupingBy(
                delta -> CanonicalExternalId.from(delta.getSlotId()),
                Collectors.mapping(
                        delta -> DeltaEntry.from(delta, jsonPatchEngine),
                        Collectors.toList()
                )
        ));
    }
}
