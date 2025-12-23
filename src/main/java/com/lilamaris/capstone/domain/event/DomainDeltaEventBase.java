package com.lilamaris.capstone.domain.event;

import com.lilamaris.capstone.domain.DomainId;
import com.lilamaris.capstone.domain.timeline.DomainDelta;

public interface DomainDeltaEventBase {
    DomainId<?, ?> domainId();

    DomainDelta.Patch toPatch();
}
