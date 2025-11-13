package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.domain.degree.Course;
import com.lilamaris.capstone.domain.timeline.Timeline;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class TimelineTest {
    private Timeline timeline;
    private List<Course> initialData;

    @BeforeEach
    void setup() {
        List<Course> initialData = List.of(
                Course.from(Course.Id.random(), "C101", "Data structure", 3),
                Course.from(Course.Id.random(), "C102", "Data science", 3),
                Course.from(Course.Id.random(), "C102", "Operating System", 3)
        );

        Timeline timeline = Timeline.from(Timeline.Id.random(), "Initial");
    }
}
