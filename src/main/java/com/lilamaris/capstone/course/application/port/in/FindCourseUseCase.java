package com.lilamaris.capstone.course.application.port.in;

import com.lilamaris.capstone.course.application.result.CourseResult;
import com.lilamaris.capstone.course.domain.id.CourseId;

import java.util.List;

public interface FindCourseUseCase {
    List<CourseResult.Query> getAll();

    CourseResult.Query getById(CourseId id);
}
