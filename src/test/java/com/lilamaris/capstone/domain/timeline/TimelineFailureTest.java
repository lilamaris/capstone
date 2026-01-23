package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.timeline.domain.exception.TimelineDomainException;
import com.lilamaris.capstone.timeline.domain.exception.TimelineErrorCode;
import com.lilamaris.capstone.util.TestSupport;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TimelineFailureTest {
    @Test
    void migrate_throws_if_no_matches_slot() {
        // given
        var timeline = TimelineTestSupport.given(
                TestSupport.t(100)
        );

        // when / then
        assertThatThrownBy(() ->
                timeline.migrate(
                        TestSupport.slotIdSupplier(),
                        TestSupport.slotClosureIdSupplier(),
                        TestSupport.t(20),
                        TestSupport.t(50)
                )
        )
                .isInstanceOf(TimelineDomainException.class)
                .hasFieldOrPropertyWithValue("errorCode", TimelineErrorCode.NO_AVAILABLE_SLOT);
    }

    @Test
    void merge_throws_if_no_candidate_slots() {
        // given
        var timeline = TimelineTestSupport.given(
                TestSupport.t(1000)
        );

        // when / then
        assertThatThrownBy(() ->
                timeline.merge(
                        TestSupport.slotIdSupplier(),
                        TestSupport.slotClosureIdSupplier(),
                        TestSupport.t(20),
                        TestSupport.t(0),
                        TestSupport.t(100)
                )
        )
                .isInstanceOf(TimelineDomainException.class)
                .hasFieldOrPropertyWithValue("errorCode", TimelineErrorCode.NO_AVAILABLE_SLOT);
    }
}
