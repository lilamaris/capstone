package com.lilamaris.capstone.course.infrastructure.web.controller;

import com.lilamaris.capstone.course.application.port.in.CreateCourseUseCase;
import com.lilamaris.capstone.course.application.port.in.DeleteCourseUseCase;
import com.lilamaris.capstone.course.application.port.in.FindCourseUseCase;
import com.lilamaris.capstone.course.application.port.in.UpdateCourseUseCase;
import com.lilamaris.capstone.course.domain.id.CourseId;
import com.lilamaris.capstone.course.infrastructure.web.request.CourseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CreateCourseUseCase createCourseUseCase;
    private final UpdateCourseUseCase updateCourseUseCase;
    private final DeleteCourseUseCase deleteCourseUseCase;

    private final FindCourseUseCase findCourseUseCase;

    @PostMapping
    public ResponseEntity<?> createCourse(
            @RequestBody CourseRequest.Create body
    ) {
        var result = createCourseUseCase.create(
                body.title(),
                body.details()
        );

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable("id") UUID id,
            @RequestBody CourseRequest.Update body
    ) {
        var courseId = new CourseId(id);
        var result = updateCourseUseCase.update(
                courseId,
                body.title(),
                body.details()
        );

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable("id") UUID id
    ) {
        var courseId = new CourseId(id);
        deleteCourseUseCase.delete(courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllCourse() {
        var result = findCourseUseCase.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(
            @PathVariable("id") UUID id
    ) {
        var courseId = new CourseId(id);
        var course = findCourseUseCase.getById(courseId);
        return ResponseEntity.ok(course);
    }
}
