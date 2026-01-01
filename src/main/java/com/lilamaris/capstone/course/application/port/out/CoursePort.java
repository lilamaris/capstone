package com.lilamaris.capstone.course.application.port.out;

import com.lilamaris.capstone.course.domain.Course;
import com.lilamaris.capstone.course.domain.id.CourseId;

import java.util.List;
import java.util.Optional;

public interface CoursePort {
    Optional<Course> getById(CourseId id);

    List<Course> getByIds(List<CourseId> ids);

    Course save(Course domain);
}
