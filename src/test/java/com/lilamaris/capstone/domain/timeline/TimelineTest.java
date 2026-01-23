package com.lilamaris.capstone.domain.timeline;

import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.event.TimelineCreated;
import com.lilamaris.capstone.util.TestSupport;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimelineTest {
    @Test
    void create() {
        // given
        var timeline = Timeline.create(
                TestSupport.timelineIdSupplier(),
                TestSupport.slotIdSupplier(),
                TestSupport.slotClosureIdSupplier(),
                "Title",
                "Details",
                TestSupport.t(0),
                TestSupport.t(0)
        );

        // then
        assertThat(timeline.getSlotList()).hasSize(1);
        assertThat(timeline.getSlotClosureList()).hasSize(1);

        var events = timeline.getEventList();
        assertThat(events)
                .hasSizeGreaterThanOrEqualTo(3)
                .anyMatch(e -> e instanceof TimelineCreated);
    }

    @Test
    void migrate_splits_slot_into_two() {
        // given
        var timeline = TimelineTestSupport.given();

        int initialSlotCount = timeline.getSlotList().size();

        // when
        timeline.migrate(
                TestSupport.slotIdSupplier(),
                TestSupport.slotClosureIdSupplier(),
                TestSupport.t(50),
                TestSupport.t(60)
        );

        // then
        int expectSlotCount = initialSlotCount + 2;

        assertThat(timeline.getSlotList())
                .hasSize(expectSlotCount);

        assertThat(timeline.getSlotClosureList())
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(expectSlotCount);

        assertThat(timeline.getEventList())
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(2 * expectSlotCount);
    }

    @Test
    void merge_closes_multiple_slots_and_create_one() {
        // given
        var timeline = TimelineTestSupport.given();

        timeline.migrate(
                TestSupport.slotIdSupplier(),
                TestSupport.slotClosureIdSupplier(),
                TestSupport.t(10),
                TestSupport.t(50)
        );

        int beforeMergeSlotCount = timeline.getSlotList().size();

        // when
        timeline.merge(
                TestSupport.slotIdSupplier(),
                TestSupport.slotClosureIdSupplier(),
                TestSupport.t(20),
                TestSupport.t(0),
                TestSupport.t(100)
        );

        // then
        assertThat(timeline.getSlotList())
                .hasSize(beforeMergeSlotCount + 1);

        assertThat(timeline.getEventList())
                .hasSizeGreaterThanOrEqualTo(beforeMergeSlotCount + 1);
    }
}
