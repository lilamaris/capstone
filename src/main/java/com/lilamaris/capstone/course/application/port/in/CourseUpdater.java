package com.lilamaris.capstone.course.application.port.in;

import com.lilamaris.capstone.course.domain.id.CourseId;

public interface CourseUpdater {
    CourseEntry update(CourseId id, String title, String details);
}
