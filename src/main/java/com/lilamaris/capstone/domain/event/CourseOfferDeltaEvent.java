package com.lilamaris.capstone.domain.event;

import com.lilamaris.capstone.application.util.JsonPatchEngine;
import com.lilamaris.capstone.domain.DomainId;
import com.lilamaris.capstone.domain.degree_course.CourseOffer;
import com.lilamaris.capstone.domain.timeline.DomainDelta;
import lombok.Builder;

@Builder(toBuilder = true)
public record CourseOfferDeltaEvent(
        CourseOffer offer
) implements DomainDeltaEventBase {
    public static CourseOfferDeltaEvent from(CourseOffer domain) {
        return new CourseOfferDeltaEvent(domain);
    }

    @Override
    public DomainId<?, ?> domainId() {
        return offer.id();
    }

    @Override
    public DomainDelta.Patch toPatch() {
        return new DomainDelta.JsonPatch(
                JsonPatchEngine.createAddPatch(offer)
        );
    }
}
