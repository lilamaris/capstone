package com.lilamaris.capstone.scenario.offer.infrastructure.web.controller;

import com.lilamaris.capstone.scenario.offer.application.port.in.OfferIssuer;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferRevoker;
import com.lilamaris.capstone.scenario.offer.infrastructure.web.request.OfferRequest;
import com.lilamaris.capstone.scenario.offer.infrastructure.web.response.OfferResponse;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferIssuer offerIssuer;
    private final OfferRevoker offerRevoker;

    @PostMapping
    public ResponseEntity<?> createOffer(
            @RequestBody OfferRequest.Offer body
    ) {
        var result = offerIssuer.offer(
                body.resourceType(),
                DefaultExternalizableId.from(body.resourceId()),
                DefaultExternalizableId.from(body.snapshotId())
        );

        return ResponseEntity.ok(
                OfferResponse.from(result)
        );
    }

    @DeleteMapping
    public ResponseEntity<?> revokeOffer(
            @RequestBody OfferRequest.Offer body
    ) {
        offerRevoker.revoke(
                body.resourceType(),
                DefaultExternalizableId.from(body.resourceId()),
                DefaultExternalizableId.from(body.snapshotId())
        );

        return ResponseEntity.noContent().build();
    }
}
