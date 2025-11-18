package com.lilamaris.capstone.application.port.in.condition;

import com.lilamaris.capstone.domain.timeline.Timeline;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
public record SnapshotQueryCondition(
        Timeline.Id timelineId,
        LocalDateTime txAt,
        LocalDateTime validAt
) {
    public SnapshotQueryCondition {
        Objects.requireNonNull(timelineId, "'timelineId' of type Timeline.Id cannot be null");
    }

    public static SnapshotQueryCondition create(Timeline.Id timelineId, LocalDateTime txAt, LocalDateTime validAt) {
        return new SnapshotQueryCondition(timelineId, txAt, validAt);
    }

    public boolean hasTxAt() { return txAt != null; }
    public boolean hasValidAt() { return validAt != null; }
    public boolean showLatest() { return txAt == null; }
}
