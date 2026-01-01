package com.lilamaris.capstone.shared.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}
