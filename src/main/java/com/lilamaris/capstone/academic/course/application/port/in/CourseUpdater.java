package com.lilamaris.capstone.academic.course.application.port.in;

import com.lilamaris.capstone.academic.course.domain.id.CourseId;

public interface CourseUpdater {
    CourseEntry update(CourseId id, String title, String details);
}
