package com.lilamaris.capstone.scenario.register_resource_on_timeline.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.lilamaris.capstone.delta.application.port.in.DeltaEntry;
import com.lilamaris.capstone.delta.application.port.in.DeltaIssuer;
import com.lilamaris.capstone.delta.application.port.in.DeltaReader;
import com.lilamaris.capstone.delta.application.port.in.DeltaRevoker;
import com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in.OfferAggregator;
import com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in.OfferEntry;
import com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in.OfferIssuer;
import com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in.OfferRevoker;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchEngine;
import com.lilamaris.capstone.shared.application.jsonPatch.DomainJsonResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.CanonicalExternalId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import com.lilamaris.capstone.timeline.application.port.in.SlotEntry;
import com.lilamaris.capstone.timeline.application.port.in.SlotPathResolver;
import com.lilamaris.capstone.timeline.application.port.in.SlotReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService implements
        OfferIssuer,
        OfferRevoker,
        OfferAggregator {
    private final SlotReader slotReader;
    private final SlotPathResolver slotPathResolver;

    private final DeltaReader deltaReader;
    private final DeltaIssuer deltaIssuer;
    private final DeltaRevoker deltaRevoker;

    private final DomainJsonResolverDirectory jsonDirs;
    private final JsonPatchEngine jsonPatchEngine;
    private final ObjectMapper objectMapper;

    @Override
    public OfferEntry offer(
            DomainRef resource,
            ExternalizableId slotId
    ) {
        JsonNode currentState = jsonDirs.resolve(resource);

        SlotEntry slot = slotReader.getById(slotId);

        Optional<JsonNode> previousState = slotPathResolver.getParent(slotId)
                .flatMap(parent -> deltaReader.getDeltaOfSlot(parent.ref().id(), resource))
                .map(DeltaEntry::state);

        JsonPatch patch = previousState
                .map(prev -> jsonPatchEngine.diff(prev, currentState))
                .orElse(null);

        JsonNode finalState = previousState
                .map(prev -> jsonPatchEngine.apply(prev, patch))
                .orElse(currentState);

        var delta = deltaIssuer.issue(
                resource,
                slotId,
                finalState,
                patch
        );

        return OfferEntry.from(slot, delta.resource());
    }

    @Override
    public void revoke(
            DomainRef resource,
            ExternalizableId slotId
    ) {
        deltaRevoker.revoke(resource, slotId);
    }

    @Override
    public void aggregate(DomainType resourceType, ExternalizableId targetSlotId) {
    }
//
//    private Optional<JsonNode> findAggregateResource(
//            DomainRef resource,
//            ExternalizableId targetSlotId
//    ) {
//        List<ExternalizableId> ancestorSlotIds = slotPathResolver.getHierarchy(targetSlotId).stream()
//                .sorted(Comparator.comparing(SlotPathEntry::depth))
//                .map(e -> e.ancestorSlotRef().id())
//                .toList();
//
//        Map<CanonicalExternalId, DeltaEntry> candidateDelta = deltaReader.getDeltaOfSlots(
//                ancestorSlotIds,
//                resource
//        );
//
//        JsonNode aggregate = null;
//        boolean found = false;
//
//        for (var id : ancestorSlotIds) {
//            var canonicalSlotId = CanonicalExternalId.from(id);
//            var d = candidateDelta.get(canonicalSlotId);
//            if (d == null) continue;
//
//            JsonPatch patch = jsonPatchEngine.parsePatch(d.patch());
//
//            if (!found) {
//                aggregate = jsonPatchEngine.materialize(patch);
//                found = true;
//            } else {
//                aggregate = jsonPatchEngine.apply(aggregate, patch);
//            }
//        }
//
//        return Optional.ofNullable(aggregate);
//    }
}