package com.lilamaris.capstone.application.port.in.result;

import com.lilamaris.capstone.domain.degree_course.Course;
import lombok.Builder;

import java.util.List;

public class CourseResult {
    @Builder
    public record Command(
            Course.Id id,
            String code,
            String name,
            Integer credit
    ) {
        public static Command from(Course domain) {
            return Command.builder()
                    .id(domain.id())
                    .code(domain.code())
                    .name(domain.name())
                    .credit(domain.credit())
                    .build();
        }
    }

    @Builder
    public record Query(
            Course.Id id,
            String code,
            String name,
            Integer credit,
            List<CourseOfferResult.Query> courseOfferList
    ) {
        public static Query from(Course domain) {
            return Query.builder()
                    .id(domain.id())
                    .code(domain.code())
                    .name(domain.name())
                    .credit(domain.credit())
                    .build();
        }
    }
}
