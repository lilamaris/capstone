package com.lilamaris.capstone.course.infrastructure.web.request;

public class CourseRequest {
    public record Create(String title, String details) {
    }

    public record Update(String title, String details) {
    }
}
