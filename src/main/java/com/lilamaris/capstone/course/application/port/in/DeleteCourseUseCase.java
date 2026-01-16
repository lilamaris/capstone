package com.lilamaris.capstone.course.application.port.in;

import com.lilamaris.capstone.course.domain.id.CourseId;

public interface DeleteCourseUseCase {
    void delete(CourseId id);
}
