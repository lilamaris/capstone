package com.lilamaris.capstone.academic.course.application.port.in;

import com.lilamaris.capstone.academic.course.domain.id.CourseId;

import java.util.List;

public interface CourseReader {
    List<CourseEntry> getAll();

    CourseEntry getById(CourseId id);
}
