package com.lilamaris.capstone.academic.course.domain.event;

import com.lilamaris.capstone.academic.course.domain.id.CourseId;
import com.lilamaris.capstone.shared.domain.event.aggregate.AggregateEvent;

import java.time.Instant;

public record CourseCreated(
        CourseId id,
        Instant occurredAt
) implements AggregateEvent {
}
