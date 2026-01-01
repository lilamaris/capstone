package com.lilamaris.capstone.course.application.port.in;

import com.lilamaris.capstone.course.application.result.CourseResult;

public interface CourseCommandUseCase {
    CourseResult.Command create(String code, String name);
}
