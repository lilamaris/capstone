package com.lilamaris.capstone.shared.domain.event.aggregate;

import com.lilamaris.capstone.shared.domain.event.DomainEvent;

import java.util.List;

public record CollectedDomainEvent(
        List<DomainEvent> events
) {
}
