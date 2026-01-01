package com.lilamaris.capstone.course.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.course.domain.Course;
import com.lilamaris.capstone.course.domain.id.CourseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, CourseId> {
}
