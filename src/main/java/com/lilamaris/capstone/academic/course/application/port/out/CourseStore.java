package com.lilamaris.capstone.academic.course.application.port.out;

import com.lilamaris.capstone.academic.course.domain.Course;
import com.lilamaris.capstone.academic.course.domain.id.CourseId;

import java.util.List;
import java.util.Optional;

public interface CourseStore {
    Optional<Course> getById(CourseId id);

    List<Course> getAll();

    List<Course> getByIds(List<CourseId> ids);

    Course save(Course domain);

    void deleteById(CourseId id);
}
