package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.timeline.domain.id.SlotId;

import java.util.List;

public interface SlotPathResolver {
    List<SlotPathEntry> getPathOf(DomainRef ref);
}
