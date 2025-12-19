package com.lilamaris.capstone.adapter.out.jpa.mapper;

import com.lilamaris.capstone.adapter.out.jpa.entity.CourseOfferEntity;
import com.lilamaris.capstone.domain.degree_course.Course;
import com.lilamaris.capstone.domain.degree_course.CourseOffer;

public class CourseOfferEntityMapper {
    public static CourseOffer toDomain(CourseOfferEntity entity) {
        var id = new CourseOffer.Id(entity.getId());
        var courseId = new Course.Id(entity.getCourseId());
        var audit = AuditEmbeddableEntityMapper.toDomain(entity);

        return CourseOffer.from(id, courseId, entity.getSemester(), audit);
    }

    public static CourseOfferEntity toEntity(CourseOffer domain) {
        return CourseOfferEntity.builder()
                .id(domain.id().getValue())
                .courseId(domain.courseId().getValue())
                .semester(domain.semester())
                .build();
    }
}
