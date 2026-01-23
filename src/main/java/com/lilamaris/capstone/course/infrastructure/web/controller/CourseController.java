package com.lilamaris.capstone.course.infrastructure.web.controller;

import com.lilamaris.capstone.course.application.port.in.CourseCreator;
import com.lilamaris.capstone.course.application.port.in.CourseReader;
import com.lilamaris.capstone.course.application.port.in.CourseRemover;
import com.lilamaris.capstone.course.application.port.in.CourseUpdater;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.course.infrastructure.web.request.CourseRequest;
import com.lilamaris.capstone.course.infrastructure.web.response.CourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseCreator courseCreator;
    private final CourseUpdater courseUpdater;
    private final CourseRemover courseRemover;

    private final CourseReader courseReader;

    @PostMapping
    public ResponseEntity<?> createCourse(
            @RequestBody CourseRequest.Create body
    ) {
        var result = courseCreator.create(
                body.title(),
                body.details()
        );

        return ResponseEntity.ok(
                CourseResponse.from(result)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable("id") UUID id,
            @RequestBody CourseRequest.Update body
    ) {
        var courseId = new CourseId(id);
        var result = courseUpdater.update(
                courseId,
                body.title(),
                body.details()
        );

        return ResponseEntity.ok(
                CourseResponse.from(result)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable("id") UUID id
    ) {
        var courseId = new CourseId(id);
        courseRemover.delete(courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllCourse() {
        var result = courseReader.getAll();
        return ResponseEntity.ok(
                result.stream().map(CourseResponse::from).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(
            @PathVariable("id") UUID id
    ) {
        var courseId = new CourseId(id);
        var result = courseReader.getById(courseId);
        return ResponseEntity.ok(
                CourseResponse.from(result)
        );
    }
}
