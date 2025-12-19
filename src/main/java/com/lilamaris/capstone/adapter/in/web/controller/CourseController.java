package com.lilamaris.capstone.adapter.in.web.controller;

import com.lilamaris.capstone.adapter.in.web.request.CourseRequest;
import com.lilamaris.capstone.application.port.in.CourseCommandUseCase;
import com.lilamaris.capstone.domain.degree_course.Course;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseCommandUseCase courseCommandUseCase;

    @PostMapping
    public ResponseEntity<?> createCourse(
            @RequestBody CourseRequest.Create body
    ) {
        var result = courseCommandUseCase.create(body.code(), body.name(), body.credit());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> createCourseOffer(
            @PathVariable("id") UUID id,
            @RequestBody CourseRequest.CreateOffer body
    ) {
        var timelineId = new Timeline.Id(body.timelineId());
        var snapshotId = new Snapshot.Id(body.snapshotId());
        var courseId = new Course.Id(id);
        var result = courseCommandUseCase.createOffer(courseId, body.semester(), timelineId, snapshotId);
        return ResponseEntity.ok(result);
    }
}
