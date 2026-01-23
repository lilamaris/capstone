package com.lilamaris.capstone.scenario.offer.application.service;

import com.lilamaris.capstone.delta.application.port.in.DeltaIssuer;
import com.lilamaris.capstone.delta.application.port.in.DeltaRevoker;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferEntry;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferIssuer;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferRevoker;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.timeline.application.port.in.SlotPathResolver;
import com.lilamaris.capstone.timeline.application.port.in.SlotReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferService implements
        OfferIssuer,
        OfferRevoker {
    private final SlotReader slotReader;
    private final SlotPathResolver slotPathResolver;

    private final DeltaIssuer deltaIssuer;
    private final DeltaRevoker deltaRevoker;
    private final JsonPatchResolverDirectory patchResolvers;

    @Override
    public OfferEntry offer(
            DomainRef resource,
            ExternalizableId slotId
    ) {
        var slot = slotReader.getById(slotId);

        var patchResolver = patchResolvers.resolverOf(resource.type());

        var jsonPatch = patchResolver.resolve(resource);

        var delta = deltaIssuer.issue(
                resource,
                slotId,
                jsonPatch
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
}
