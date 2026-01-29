package com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.infrastructure.web.controller;

import com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.application.port.in.OfferAggregator;
import com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.application.port.in.OfferIssuer;
import com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.application.port.in.OfferRevoker;
import com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.infrastructure.web.request.OfferRequest;
import com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.infrastructure.web.response.OfferAggregateResponse;
import com.lilamaris.capstone.bitemporal.scenario.register_resource_on_timeline.infrastructure.web.response.OfferResponse;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotId;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainRef;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
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
        var result = aggregator.aggregate(
                body.resourceType(),
                slotId.externalId()
        );

        return ResponseEntity.ok(
                result.stream().map(OfferAggregateResponse::from).toList()
        );
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
