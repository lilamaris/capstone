package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;

import java.time.Instant;
import java.util.List;

public interface SlotReader {
    List<SlotEntry> resolveRefs(List<DomainRef> refs);

    SlotEntry resolveRef(DomainRef ref);

    List<SlotEntry> getByTimelineInTxTime(TimelineId timelineId, Instant at);

    List<SlotEntry> getByTimelineInValidTime(TimelineId timelineId, Instant at);
}
