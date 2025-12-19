package com.lilamaris.capstone.domain.degree_organization;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainType;
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
) {
    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "organization.academic-program";
        }
    }

    public static class Id extends AbstractUUIDDomainId<Type> {
        public Id(UUID value) {
            super(value);
        }

        public Id() {
            super();
        }

        @Override
        public Type getDomainType() {
            return Type.INSTANCE;
        }
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