package com.lilamaris.capstone.course.application.port.in;

import com.lilamaris.capstone.course.application.result.CourseResult;
import com.lilamaris.capstone.course.domain.id.CourseId;

public interface UpdateCourseUseCase {
    CourseResult.Command update(CourseId id, String title, String details);
}
