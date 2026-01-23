package com.lilamaris.capstone.course.application.port.in;

public interface CourseCreator {
    CourseEntry create(String title, String details);
}
