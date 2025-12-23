package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.degree_course.Course;

import java.util.List;
import java.util.Optional;

public interface CoursePort {
    Optional<Course> getById(Course.Id id);

    List<Course> getByIds(List<Course.Id> ids);

    Course save(Course domain);
}
