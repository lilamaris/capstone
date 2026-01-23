package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.util.TestSupport;

import java.time.Instant;

import static com.lilamaris.capstone.util.TestSupport.t;

public class TimelineTestSupport {
    static Timeline given(
            Instant txAt,
            Instant validAt
    ) {
        return Timeline.create(
                TestSupport.timelineIdSupplier(),
                TestSupport.slotIdSupplier(),
                TestSupport.slotClosureIdSupplier(),
                "Title",
                "Details",
                txAt,
                validAt
        );
    }

    static Timeline given(Instant validAt) {
        return given(t(0), validAt);
    }

    static Timeline given() {
        return given(t(0), t(0));
    }
}
