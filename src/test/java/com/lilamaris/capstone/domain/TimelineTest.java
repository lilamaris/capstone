package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.domain.degree_course.Course;
import com.lilamaris.capstone.domain.timeline.Timeline;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class TimelineTest {
    private Timeline timeline;
    private List<Course> initialData;

    @BeforeEach
    void setup() {
        List<Course> initialData = List.of(
                Course.create("C101", "Data structure", 3),
                Course.create("C102", "Data science", 3),
                Course.create("C102", "Operating System", 3)
        );

        Timeline timeline = Timeline.create("Initial");
    }
}
