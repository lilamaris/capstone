package com.lilamaris.capstone.domain.model.common.event;

import java.util.List;

public record CollectedDomainEvent(
        List<DomainEvent> events
) {
}
