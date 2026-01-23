package com.lilamaris.capstone.course.application.port.in;

import com.lilamaris.capstone.course.domain.id.CourseId;

import java.util.List;

public interface CourseReader {
    List<CourseEntry> getAll();

    CourseEntry getById(CourseId id);
}
