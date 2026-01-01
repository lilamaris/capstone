package com.lilamaris.capstone.course.application.result;

import com.lilamaris.capstone.course.domain.Course;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.shared.application.result.AuditResult;
import com.lilamaris.capstone.shared.application.result.DescriptionResult;

public class CourseResult {
    public record Command(
            CourseId id,
            DescriptionResult description,
            AuditResult audit
    ) {
        public static Command from(Course domain) {
            return new Command(
                    domain.id(),
                    DescriptionResult.from(domain),
                    AuditResult.from(domain)
            );
        }
    }

    public record Query(
            CourseId id,
            DescriptionResult description,
            AuditResult audit
    ) {
        public static Query from(Course domain) {
            return new Query(
                    domain.id(),
                    DescriptionResult.from(domain),
                    AuditResult.from(domain)
            );
        }
    }
}
