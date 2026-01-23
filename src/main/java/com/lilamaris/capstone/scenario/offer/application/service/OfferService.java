package com.lilamaris.capstone.scenario.offer.application.service;

import com.lilamaris.capstone.resource_offer.application.port.in.ResourceOfferCreator;
import com.lilamaris.capstone.resource_offer.application.port.in.ResourceOfferRemover;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferEntry;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferIssuer;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferRevoker;
import com.lilamaris.capstone.scenario.offer.application.port.in.SlotOfferAggregator;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchResolverDirectory;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotReader;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferService implements
        OfferIssuer,
        OfferRevoker {
    private final SnapshotReader snapshotReader;
    private final ResourceOfferCreator resourceOfferCreator;
    private final ResourceOfferRemover resourceOfferRemover;
    private final JsonPatchResolverDirectory patchResolvers;

    @Override
    public OfferEntry offer(
            DomainType resourceType,
            ExternalizableId resourceId,
            SnapshotId snapshotId
    ) {
        var resource = DefaultDomainRef.from(resourceType, resourceId);
        var snapshot = snapshotReader.getById(snapshotId);
        var patchResolver = patchResolvers.resolverOf(resource.type());
        var jsonPatch = patchResolver.resolve(resource);
        var resourceOfferEntry = resourceOfferCreator.issue(resource, snapshotId, jsonPatch);
        return OfferEntry.from(snapshot, resourceOfferEntry.resource());
    }

    @Override
    public void revoke(
            DomainType resourceType,
            ExternalizableId resourceId,
            SnapshotId snapshotId
    ) {
        var resource = DefaultDomainRef.from(resourceType, resourceId);
        resourceOfferRemover.revoke(resource, snapshotId);
    }
}
