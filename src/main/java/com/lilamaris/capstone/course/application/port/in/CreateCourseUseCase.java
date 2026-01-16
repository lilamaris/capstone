package com.lilamaris.capstone.course.application.port.in;

import com.lilamaris.capstone.course.application.result.CourseResult;

public interface CreateCourseUseCase {
    CourseResult.Command create(String title, String details);
}
