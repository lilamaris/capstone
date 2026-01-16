package com.lilamaris.capstone.course.application.service;

import com.lilamaris.capstone.course.application.port.in.CreateCourseUseCase;
import com.lilamaris.capstone.course.application.port.in.DeleteCourseUseCase;
import com.lilamaris.capstone.course.application.port.in.UpdateCourseUseCase;
import com.lilamaris.capstone.course.application.port.out.CourseStore;
import com.lilamaris.capstone.course.application.result.CourseResult;
import com.lilamaris.capstone.course.domain.Course;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDescriptionMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseCommandService implements
        CreateCourseUseCase,
        UpdateCourseUseCase,
        DeleteCourseUseCase {
    private final CourseStore courseStore;

    @Override
    public CourseResult.Command create(String title, String details) {
        var created = Course.create(title, details);
        var saved = courseStore.save(created);
        return CourseResult.Command.from(saved);
    }

    @Override
    public CourseResult.Command update(CourseId id, String title, String details) {
        var course = courseStore.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Course with id '%s' not found.", id
                )));
        course.updateDescription(new DefaultDescriptionMetadata(title, details));
        var saved = courseStore.save(course);
        return CourseResult.Command.from(saved);
    }

    @Override
    public void delete(CourseId id) {
        courseStore.deleteById(id);
    }
}
