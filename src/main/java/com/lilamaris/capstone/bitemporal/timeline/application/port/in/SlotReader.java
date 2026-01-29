package com.lilamaris.capstone.bitemporal.timeline.application.port.in;

import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

import java.time.Instant;
import java.util.List;

public interface SlotReader {
    List<SlotEntry> resolveRefs(List<DomainRef> refs);

    SlotEntry resolveRef(DomainRef ref);

    SlotEntry getById(ExternalizableId id);

    List<SlotEntry> getByTimelineInTxTime(TimelineId timelineId, Instant at);

    List<SlotEntry> getByTimelineInValidTime(TimelineId timelineId, Instant at);
}
