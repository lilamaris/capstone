package com.lilamaris.capstone.domain.model.common.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}
