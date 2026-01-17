package com.lilamaris.capstone.scenario.offer.application.result;

import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;

public class OfferResult {
    public record Command(
            String snapshotId
    ) {
    }
}
