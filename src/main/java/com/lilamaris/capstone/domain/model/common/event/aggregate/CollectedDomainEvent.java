package com.lilamaris.capstone.domain.model.common.event.aggregate;

import com.lilamaris.capstone.domain.model.common.event.DomainEvent;

import java.util.List;

public record CollectedDomainEvent(
        List<DomainEvent> events
) {
}
