package com.lilamaris.capstone.academic.course.application.port.in;

import com.lilamaris.capstone.academic.course.domain.Course;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;

public record CourseEntry(
        DomainRef courseRef,
        DescriptionMetadata descriptionMetadata,
        AuditMetadata auditMetadata
) {
    public static CourseEntry from(Course course) {
        return new CourseEntry(
                course.id().ref(),
                course.descriptionMetadata(),
                course.auditMetadata()
        );
    }
}
