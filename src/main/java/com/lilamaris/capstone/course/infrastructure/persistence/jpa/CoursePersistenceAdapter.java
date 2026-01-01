package com.lilamaris.capstone.course.infrastructure.persistence.jpa;

import com.lilamaris.capstone.course.application.port.out.CoursePort;
import com.lilamaris.capstone.course.domain.Course;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.course.infrastructure.persistence.jpa.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CoursePort {
    private final CourseRepository repository;

    @Override
    public Optional<Course> getById(CourseId id) {
        return repository.findById(id);
    }

    @Override
    public List<Course> getByIds(List<CourseId> ids) {
        return repository.findAllById(ids);
    }

    @Override
    @Transactional
    public Course save(Course domain) {
        return repository.save(domain);
    }
}
