package com.lilamaris.capstone.domain.degree_course;

import com.lilamaris.capstone.domain.BaseDomain;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record CourseOffer(
        Id id,
        Course.Id courseId,
        Integer semester,
        Boolean isRetire
) implements BaseDomain<CourseOffer.Id, CourseOffer> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public CourseOffer {
        id = Optional.ofNullable(id).orElseGet(Id::random);

        courseId = Optional.ofNullable(courseId).orElseThrow(() -> new IllegalArgumentException("'courseId' of type Course.Id cannot be null"));
        semester = Optional.ofNullable(semester).orElseThrow(() -> new IllegalArgumentException("'semester' of type Integer cannot be null"));

        isRetire = Optional.ofNullable(isRetire).orElse(false);
    }

    public static CourseOffer from(Id id, Course.Id courseId, Integer semester) {
        return getDefaultBuilder(courseId, semester).id(id).build();
    }

    public static CourseOffer create(Course.Id courseId, Integer semester) {
        return getDefaultBuilder(courseId, semester).build();
    }

    public CourseOffer retire() {
        return toBuilder().isRetire(true).build();
    }

    private static CourseOfferBuilder getDefaultBuilder(Course.Id courseId, Integer semester) {
        return builder().courseId(courseId).semester(semester);
    }
}
