package com.lilamaris.capstone.domain.model.common.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}
