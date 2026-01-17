package com.lilamaris.capstone.scenario.offer.application.service;

import com.lilamaris.capstone.resource_offer.application.port.in.ResourceOfferRemover;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferRevokeUseCase;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferRevokeService implements OfferRevokeUseCase {
    private final ResourceOfferRemover resourceOfferRemover;

    @Override
    public void revoke(DomainRef resource, ExternalizableId snapshotId) {

    }
}
