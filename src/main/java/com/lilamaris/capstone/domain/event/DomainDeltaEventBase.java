package com.lilamaris.capstone.domain.event;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.timeline.DomainDelta;

public interface DomainDeltaEventBase {
    String domainType();
    BaseDomain.Id<?> domainId();
    DomainDelta.Patch toPatch();
}
