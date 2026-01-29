package com.lilamaris.capstone.academic.course.application.port.in;

public interface CourseCreator {
    CourseEntry create(String title, String details);
}
