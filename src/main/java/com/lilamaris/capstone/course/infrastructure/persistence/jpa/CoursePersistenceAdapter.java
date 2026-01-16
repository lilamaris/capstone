package com.lilamaris.capstone.course.infrastructure.persistence.jpa;

import com.lilamaris.capstone.course.application.port.out.CourseStore;
import com.lilamaris.capstone.course.domain.Course;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.course.infrastructure.persistence.jpa.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CourseStore {
    private final CourseRepository repository;

    @Override
    public Optional<Course> getById(CourseId id) {
        return repository.findById(id);
    }

    @Override
    public List<Course> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Course> getByIds(List<CourseId> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Course save(Course domain) {
        return repository.save(domain);
    }

    @Override
    public void deleteById(CourseId id) {
        repository.deleteById(id);
    }
}
