package com.lilamaris.capstone.academic.course.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.academic.course.domain.Course;
import com.lilamaris.capstone.academic.course.domain.id.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, CourseId> {
}
