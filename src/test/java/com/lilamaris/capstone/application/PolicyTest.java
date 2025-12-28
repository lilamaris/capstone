package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.policy.identity.defaults.DefaultIdGenerationContext;
import com.lilamaris.capstone.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.capstone.course.id.CourseId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.util.SequentialUuidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

public class PolicyTest {
    private IdGenerationContext ids;

    @BeforeEach
    void run() {
        var timelineIdGen = new RawBasedIdGenerator<>(TimelineId.class, TimelineId::new, new SequentialUuidGenerator());
        ids = new DefaultIdGenerationContext(List.of(timelineIdGen));
    }

    @Test
    void should_create_single_policy() {
    }

    @Test
    void should_create() {
    }
}
