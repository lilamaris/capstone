package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.degree_course.Course;
import com.lilamaris.capstone.domain.degree_course.CourseOffer;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import lombok.Builder;

public class CourseOfferResult {
    @Builder
    public record Command(
            CourseOffer.Id id,
            Course.Id courseId,
            Integer semester,
            AuditResult audit
    ) {
        public static Command from(CourseOffer domain) {
            return builder()
                    .id(domain.id())
                    .courseId(domain.courseId())
                    .semester(domain.semester())
//                    .audit(AuditResult.from(domain.audit()))
                    .build();
        }
    }

    @Builder
    public record Query(
            CourseOffer.Id id,
            Snapshot.Id snapshotId,
            Course.Id courseId,
            Integer semester,
            AuditResult audit
    ) {
        public static Query from(CourseOffer domain) {
            return builder()
                    .id(domain.id())
                    .courseId(domain.courseId())
                    .semester(domain.semester())
//                    .audit(AuditResult.from(domain.audit()))
                    .build();
        }
    }
}
