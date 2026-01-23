package com.lilamaris.capstone.scenario.offer.infrastructure.web.controller;

import com.lilamaris.capstone.scenario.offer.application.port.in.OfferAggregator;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferIssuer;
import com.lilamaris.capstone.scenario.offer.application.port.in.OfferRevoker;
import com.lilamaris.capstone.scenario.offer.infrastructure.web.request.OfferRequest;
import com.lilamaris.capstone.scenario.offer.infrastructure.web.response.OfferResponse;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainRef;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferIssuer issuer;
    private final OfferRevoker revoker;
    private final OfferAggregator aggregator;

    @GetMapping
    public ResponseEntity<?> aggregateOffer(
            @RequestBody OfferRequest.Aggregate body
    ) {
        var slotId = new SlotId(UUID.fromString(body.slotId()));
        aggregator.aggregate(
                body.resourceType(),
                slotId
        );

        return ResponseEntity.ok(null);
    }

    @PostMapping
    public ResponseEntity<?> createOffer(
            @RequestBody OfferRequest.Offer body
    ) {
        var resource = DefaultDomainRef.from(
                body.resourceType(),
                body.resourceId()
        );
        var slotId = DefaultExternalizableId.from(body.slotId());

        var result = issuer.offer(
                resource,
                slotId
        );

        return ResponseEntity.ok(
                OfferResponse.from(result)
        );
    }

    @DeleteMapping
    public ResponseEntity<?> revokeOffer(
            @RequestBody OfferRequest.Offer body
    ) {
        var resource = DefaultDomainRef.from(
                body.resourceType(),
                body.resourceId()
        );
        var slotId = DefaultExternalizableId.from(body.slotId());

        revoker.revoke(
                resource,
                slotId
        );

        return ResponseEntity.noContent().build();
    }
}
