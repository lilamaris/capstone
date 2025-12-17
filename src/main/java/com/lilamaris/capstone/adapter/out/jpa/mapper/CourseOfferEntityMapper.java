package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.CourseOfferEntity;
import com.lilamaris.capstone.domain.degree_course.Course;
import com.lilamaris.capstone.domain.degree_course.CourseOffer;

public class CourseOfferEntityMapper {
    public static CourseOffer toDomain(CourseOfferEntity entity) {
        var id = CourseOffer.Id.from(entity.getId());
        var courseId = Course.Id.from(entity.getCourseId());
        var audit = AuditEmbeddableEntityMapper.toDomain(entity);

        return CourseOffer.from(id, courseId, entity.getSemester(), audit);
    }

    public static CourseOfferEntity toEntity(CourseOffer domain) {
        return CourseOfferEntity.builder()
                .id(domain.id().value())
                .courseId(domain.courseId().value())
                .semester(domain.semester())
                .build();
    }
}
