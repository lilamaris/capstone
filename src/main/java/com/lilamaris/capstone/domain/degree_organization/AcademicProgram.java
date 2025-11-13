package com.lilamaris.capstone.domain.degree_organization;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.degree_course.Course;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record AcademicProgram (
        Id id,
        String name,
        ProgramType programType,
        List<CourseOffer> courseOfferList,
        Organization.Id organizationId
) implements BaseDomain<AcademicProgram.Id, AcademicProgram> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public AcademicProgram {
        programType = Optional.ofNullable(programType).orElseThrow(() -> new IllegalArgumentException("program type must be set"));
        courseOfferList = Optional.ofNullable(courseOfferList).orElseGet(ArrayList::new);
        organizationId = Optional.ofNullable(organizationId).orElseThrow(() -> new IllegalArgumentException("organization id must be set"));
    }

    @Builder
    public record CourseOffer(
            Course.Id courseId,
            Integer semester
    ) {}

    public static AcademicProgram from(ProgramType programType, List<CourseOffer> courseOfferList) {
        return builder()
                .programType(programType)
                .courseOfferList(courseOfferList)
                .build();
    }
}