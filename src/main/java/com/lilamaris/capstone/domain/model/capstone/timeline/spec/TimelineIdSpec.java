package com.lilamaris.capstone.domain.model.capstone.timeline.spec;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.id.IdSpec;

import java.util.UUID;

public class TimelineIdSpec implements IdSpec<TimelineId, UUID> {
    @Override
    public TimelineId fromRaw(UUID raw) {
        return new TimelineId(raw);
    }
}
