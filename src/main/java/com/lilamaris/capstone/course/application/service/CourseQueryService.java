package com.lilamaris.capstone.course.application.service;

import com.lilamaris.capstone.course.application.port.in.FindCourseUseCase;
import com.lilamaris.capstone.course.application.port.out.CourseStore;
import com.lilamaris.capstone.course.application.result.CourseResult;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseQueryService implements
        FindCourseUseCase {
    private final CourseStore courseStore;


    @Override
    public List<CourseResult.Query> getAll() {
        return courseStore.getAll().stream().map(CourseResult.Query::from).toList();
    }

    @Override
    public CourseResult.Query getById(CourseId id) {
        var course = courseStore.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Course with id '%s' not found.", id
                )));
        return CourseResult.Query.from(course);
    }
}
