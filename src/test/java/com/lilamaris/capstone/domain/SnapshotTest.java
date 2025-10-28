package com.lilamaris.capstone.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
