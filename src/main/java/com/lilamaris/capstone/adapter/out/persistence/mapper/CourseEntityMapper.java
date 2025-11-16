package com.lilamaris.capstone.adapter.out.persistence.mapper;

import com.lilamaris.capstone.adapter.out.persistence.entity.CourseEntity;
import com.lilamaris.capstone.domain.degree_course.Course;

public class CourseEntityMapper {
    public static Course toDomain(CourseEntity entity) {
        var id = Course.Id.from(entity.getId());
        var courseOfferList = entity.getCourseOfferList().stream().map(CourseOfferEntityMapper::toDomain).toList();
        return Course.from(id, entity.getCode(), entity.getName(), entity.getCredit(), courseOfferList);
    }

    public static CourseEntity toEntity(Course domain) {
        return CourseEntity.builder()
                .id(domain.id().value())
                .code(domain.code())
                .name(domain.name())
                .credit(domain.credit())
                .build();
    }
}
