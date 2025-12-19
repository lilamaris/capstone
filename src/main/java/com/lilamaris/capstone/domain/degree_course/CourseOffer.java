package com.lilamaris.capstone.domain.degree_course;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainType;
import com.lilamaris.capstone.domain.embed.Audit;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record CourseOffer(
        Id id,
        Course.Id courseId,
        Integer semester,
        Boolean isRetire,
        Audit audit
) {
    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "course-offer";
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

    public CourseOffer {
        if (courseId == null) throw new DomainIllegalArgumentException("Field 'courseId' must not be null.");
        if (semester == null) throw new DomainIllegalArgumentException("Field 'semester' must not be null.");

        id = Optional.ofNullable(id).orElseGet(Id::new);
        isRetire = Optional.ofNullable(isRetire).orElse(false);
    }

    public static CourseOffer from(Id id, Course.Id courseId, Integer semester, Audit audit) {
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
