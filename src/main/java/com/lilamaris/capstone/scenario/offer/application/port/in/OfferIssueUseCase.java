package com.lilamaris.capstone.scenario.offer.application.port.in;

import com.lilamaris.capstone.scenario.offer.application.result.OfferResult;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

public interface OfferIssueUseCase {
    OfferResult.Command offer(
            DomainRef resource,
            ExternalizableId snapshotId
    );
}
