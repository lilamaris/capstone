package com.lilamaris.capstone.scenario.offer.infrastructure.web.controller;

import com.lilamaris.capstone.scenario.offer.application.port.in.OfferIssueUseCase;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferRevokeUseCase;
import com.lilamaris.capstone.scenario.offer.infrastructure.web.request.OfferRequest;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainRef;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferIssueUseCase offerIssueUseCase;
    private final OfferRevokeUseCase offerRevokeUseCase;

    @PostMapping
    public ResponseEntity<?> createOffer(
            @RequestBody OfferRequest.Offer body
    ) {
        var ref = DefaultDomainRef.from(
                body.resourceType(),
                DefaultExternalizableId.from(body.resourceId())
        );

        var result = offerIssueUseCase.offer(
                ref,
                DefaultExternalizableId.from(body.snapshotId())
        );

        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<?> revokeOffer(
            @RequestBody OfferRequest.Offer body
    ) {
        var resourceId = DefaultExternalizableId.from(body.resourceId());
        var snapshotId = DefaultExternalizableId.from(body.snapshotId());
        var ref = DefaultDomainRef.from(
                body.resourceType(),
                resourceId
        );

        var result = offerRevokeUseCase.revoke(
                ref,
                snapshotId
        );

        return ResponseEntity.ok(result);
    }
}
