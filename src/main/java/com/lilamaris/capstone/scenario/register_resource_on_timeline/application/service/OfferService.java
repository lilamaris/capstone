package com.lilamaris.capstone.scenario.register_resource_on_timeline.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.lilamaris.capstone.delta.application.port.in.*;
import com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in.OfferAggregator;
import com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in.OfferEntry;
import com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in.OfferIssuer;
import com.lilamaris.capstone.scenario.register_resource_on_timeline.application.port.in.OfferRevoker;
import com.lilamaris.capstone.shared.application.jsonPatch.DomainJsonResolverDirectory;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchEngine;
import com.lilamaris.capstone.shared.domain.id.CanonicalExternalId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import com.lilamaris.capstone.timeline.application.port.in.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public OfferEntry offer(
            DomainRef resource,
            ExternalizableId targetSlotId
    ) {
        SlotEntry slot = slotReader.getById(targetSlotId);
        JsonNode currentState = jsonDirs.resolve(resource);

        var pathOption = SlotPathResolverOption.max(targetSlotId, 1);
        var hierarchy = slotPathResolver.getHierarchy(pathOption);
        var slotIds = hierarchy.stream().map(e -> e.ref().id()).toList();

        var deltaOption = DeltaReadOption.idAndType(slotIds, resource.type(), resource.id());
        var deltas = deltaReader.getDelta(deltaOption);

        JsonNode previousState = null;

        for (SlotPathEntry entry : hierarchy) {
            var key = CanonicalExternalId.from(entry.ref().id());

            List<DeltaEntry> slotDeltas = deltas.get(key);
            if (slotDeltas == null || slotDeltas.isEmpty()) continue;

            previousState = slotDeltas.getFirst().state();
            break;
        }

        // calculate diff between previous and current if previousState provided.
        JsonPatch patch = previousState != null
                ? jsonPatchEngine.diff(previousState, currentState)
                : null;

        // determine the current final state based on whether the previous state exists.
        JsonNode finalState = previousState != null
                ? jsonPatchEngine.apply(previousState, patch)
                : currentState;

        var delta = deltaIssuer.issue(
                resource,
                targetSlotId,
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