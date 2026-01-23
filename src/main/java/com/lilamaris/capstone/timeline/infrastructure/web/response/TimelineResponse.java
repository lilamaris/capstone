package com.lilamaris.capstone.timeline.infrastructure.web.response;

import com.lilamaris.capstone.shared.infrastructure.web.response.AuditResponse;
import com.lilamaris.capstone.shared.infrastructure.web.response.DescriptionResponse;
import com.lilamaris.capstone.shared.infrastructure.web.response.DomainRefResponse;
import com.lilamaris.capstone.timeline.application.port.in.TimelineEntry;

public record TimelineResponse(
        DomainRefResponse ref,
        DescriptionResponse description,
        AuditResponse audit
) {
    public static TimelineResponse from(TimelineEntry timelineEntry) {
        var ref = DomainRefResponse.from(timelineEntry.timelineRef());
        var description = DescriptionResponse.from(timelineEntry.descriptionMetadata());
        var audit = AuditResponse.from(timelineEntry.auditMetadata());
        return new TimelineResponse(
                ref,
                description,
                audit
        );
    }
}
