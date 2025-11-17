package com.lilamaris.capstone.application.port.in.condition;

import com.lilamaris.capstone.domain.timeline.Timeline;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SnapshotQueryCondition(
        Timeline.Id timelineId,
        LocalDateTime txAt,
        LocalDateTime validAt
) {
    public boolean hasTxAt() { return txAt != null; }
    public boolean hasValidAt() { return validAt != null; }
}
