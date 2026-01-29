package com.lilamaris.capstone.bitemporal.timeline.application.port.in;

import com.lilamaris.capstone.bitemporal.timeline.domain.Timeline;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;

public record TimelineEntry(
        DomainRef timelineRef,
        DescriptionMetadata descriptionMetadata,
        AuditMetadata auditMetadata
) {
    public static TimelineEntry from(Timeline timeline) {
        return new TimelineEntry(
                timeline.id().ref(),
                timeline.descriptionMetadata(),
                timeline.auditMetadata()
        );
    }
}
