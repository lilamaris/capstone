package com.lilamaris.capstone.adapter.out.jpa;

import com.lilamaris.capstone.adapter.out.jpa.mapper.CourseEntityMapper;
import com.lilamaris.capstone.adapter.out.jpa.repository.CourseRepository;
import com.lilamaris.capstone.application.port.out.CoursePort;
import com.lilamaris.capstone.domain.degree_course.Course;
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
    public Optional<Course> getById(Course.Id id) {
        return repository.findById(id.value()).map(CourseEntityMapper::toDomain);
    }

    @Override
    public List<Course> getByIds(List<Course.Id> ids) {
        return repository.findAllById(ids.stream().map(Course.Id::value).toList()).stream().map(CourseEntityMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public Course save(Course domain) {
        var entity = CourseEntityMapper.toEntity(domain);
        var saved = repository.save(entity);
        return CourseEntityMapper.toDomain(saved);
    }
}
