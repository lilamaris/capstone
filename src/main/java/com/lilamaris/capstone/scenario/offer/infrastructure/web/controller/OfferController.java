package com.lilamaris.capstone.scenario.offer.infrastructure.web.controller;

import com.lilamaris.capstone.scenario.offer.application.port.in.OfferIssueUseCase;
import com.lilamaris.capstone.scenario.offer.infrastructure.web.request.OfferRequest;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainRef;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferIssueUseCase offerIssueUseCase;

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
}
