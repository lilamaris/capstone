package com.lilamaris.capstone.resource_offer.application.service;

import com.lilamaris.capstone.resource_offer.application.port.in.ResourceOfferRemover;
import com.lilamaris.capstone.resource_offer.application.port.out.ResourceOfferStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceOfferRemoverService implements ResourceOfferRemover {
    private final ResourceOfferStore resourceOfferStore;
}
