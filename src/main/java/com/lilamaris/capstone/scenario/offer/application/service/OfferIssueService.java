package com.lilamaris.capstone.scenario.offer.application.service;

import com.lilamaris.capstone.resource_offer.application.port.in.ResourceOfferCreator;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferIssueUseCase;
import com.lilamaris.capstone.scenario.offer.application.result.OfferResult;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferIssueService implements OfferIssueUseCase {
    private final ResourceOfferCreator resourceOfferCreator;
    private final JsonPatchResolverDirectory patchResolvers;

    @Override
    public OfferResult.Command offer(
            DomainRef resource,
            ExternalizableId snapshotId
    ) {
        var patchResolver = patchResolvers.resolverOf(resource.type());
        var jsonPatch = patchResolver.resolve(resource);
        var resourceOfferEntry = resourceOfferCreator.issue(resource, snapshotId, jsonPatch);
        return new OfferResult.Command(resourceOfferEntry.snapshotId().asString());
    }
}
