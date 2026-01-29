package com.lilamaris.capstone.academic.course.infrastructure.web.response;

import com.lilamaris.capstone.academic.course.application.port.in.CourseEntry;
import com.lilamaris.capstone.shared.infrastructure.web.response.AuditResponse;
import com.lilamaris.capstone.shared.infrastructure.web.response.DescriptionResponse;
import com.lilamaris.capstone.shared.infrastructure.web.response.DomainRefResponse;

public record CourseResponse(
        DomainRefResponse ref,
        DescriptionResponse description,
        AuditResponse audit
) {
    public static CourseResponse from(CourseEntry courseEntry) {
        var ref = DomainRefResponse.from(courseEntry.courseRef());
        var description = DescriptionResponse.from(courseEntry.descriptionMetadata());
        var audit = AuditResponse.from(courseEntry.auditMetadata());
        return new CourseResponse(
                ref,
                description,
                audit
        );
    }
}
