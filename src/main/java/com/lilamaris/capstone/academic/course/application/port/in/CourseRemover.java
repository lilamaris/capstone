package com.lilamaris.capstone.academic.course.application.port.in;

import com.lilamaris.capstone.academic.course.domain.id.CourseId;

public interface CourseRemover {
    void delete(CourseId id);
}
