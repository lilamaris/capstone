package com.lilamaris.capstone.util;

import com.lilamaris.capstone.shared.application.policy.domain.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.timeline.domain.id.SlotClosureId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.function.Supplier;

public class TestSupport {
    private static final IdGenerator<TimelineId> timelineIdIdGenerator = new RawBasedIdGenerator<>(TimelineId.class, TimelineId::new, new SequentialUuidGenerator());
    private static final IdGenerator<SlotId> slotIdIdGenerator = new RawBasedIdGenerator<>(SlotId.class, SlotId::new, new SequentialUuidGenerator());
    private static final IdGenerator<SlotClosureId> slotClosureIdIdGenerator = new RawBasedIdGenerator<>(SlotClosureId.class, SlotClosureId::new, new SequentialUuidGenerator());

    public static Supplier<TimelineId> timelineIdSupplier() {
        return timelineIdIdGenerator::next;
    }

    public static Supplier<SlotId> slotIdSupplier() {
        return slotIdIdGenerator::next;
    }

    public static Supplier<SlotClosureId> slotClosureIdSupplier() {
        return slotClosureIdIdGenerator::next;
    }

    public static Instant t(int sec) {
        return Instant.ofEpochSecond(sec);
    }
}
