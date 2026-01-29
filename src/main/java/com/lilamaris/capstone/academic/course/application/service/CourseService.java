package com.lilamaris.capstone.academic.course.application.service;

import com.lilamaris.capstone.academic.course.application.policy.privilege.CourseAction;
import com.lilamaris.capstone.academic.course.application.port.in.*;
import com.lilamaris.capstone.academic.course.application.port.out.CourseStore;
import com.lilamaris.capstone.academic.course.domain.Course;
import com.lilamaris.capstone.academic.course.domain.id.CourseId;
import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAuthorizer;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDescriptionMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService implements
        CourseReader,
        CourseCreator,
        CourseUpdater,
        CourseRemover {
    private final CourseStore courseStore;
    private final ResourceAuthorizer authorizer;

    @Override
    public List<CourseEntry> getAll() {
        return courseStore.getAll().stream().map(CourseEntry::from).toList();
    }

    @Override
    public CourseEntry getById(CourseId id) {
        var course = courseStore.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Course with id '%s' not found.", id
                )));
        return CourseEntry.from(course);
    }

    @Override
    public CourseEntry create(String title, String details) {
        var created = Course.create(title, details);
        var saved = courseStore.save(created);
        return CourseEntry.from(saved);
    }

    @Override
    public CourseEntry update(CourseId id, String title, String details) {
        var actor = ActorContext.get();
        authorizer.authorize(actor, id.ref(), CourseAction.UPDATE_METADATA);

        var course = courseStore.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Course with id '%s' not found.", id
                )));
        course.updateDescription(new DefaultDescriptionMetadata(title, details));
        var saved = courseStore.save(course);
        return CourseEntry.from(saved);
    }

    @Override
    public void delete(CourseId id) {
        courseStore.deleteById(id);
    }
}
