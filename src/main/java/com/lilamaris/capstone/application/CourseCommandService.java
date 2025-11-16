package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.port.in.CourseCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.CourseResult;
import com.lilamaris.capstone.application.port.out.CoursePort;
import com.lilamaris.capstone.domain.degree_course.Course;
import com.lilamaris.capstone.domain.event.SnapshotDeltaEvent;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class CourseCommandService implements CourseCommandUseCase {
    private final CoursePort coursePort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CourseResult.Command create(String code, String name, Integer credit) {
        var created = Course.create(code, name, credit);
        var saved = coursePort.save(created);
        return CourseResult.Command.from(saved);
    }

    @Override
    public CourseResult.Command createOffer(Course.Id id, Integer semester, Timeline.Id timelineId, Snapshot.Id snapshotId) {
        var course = coursePort.getById(id).orElseThrow(EntityNotFoundException::new);
        var updated = course.offer(semester);

        updated.pullEvent().forEach(e -> {
            var event = SnapshotDeltaEvent.fromBase(timelineId, snapshotId, e);
            eventPublisher.publishEvent(event);
        });

        var saved = coursePort.save(updated);

        return CourseResult.Command.from(saved);
    }
}
