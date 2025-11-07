package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.domain.timeline.Timeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SnapshotTest {
    private Timeline.Id timelineId;

    @BeforeEach
    void setup() {
        timelineId = Timeline.Id.random();
    }

    @Test
    void should_create() {

    }
}
