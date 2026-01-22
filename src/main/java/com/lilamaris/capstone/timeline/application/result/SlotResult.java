package com.lilamaris.capstone.timeline.application.result;

import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

public record SlotResult(
        SlotId id,
        TimelineId timelineId,
        SlotId parentSlotId,
        EffectiveResult tx,
        EffectiveResult valid
) {
    public static SlotResult from(Slot domain) {
        var txResult = EffectiveResult.from(domain.getTx());
        var validResult = EffectiveResult.from(domain.getValid());
        return new SlotResult(
                domain.id(),
                domain.getTimelineId(),
                domain.getParentSlotId(),
                txResult,
                validResult
        );
    }
}