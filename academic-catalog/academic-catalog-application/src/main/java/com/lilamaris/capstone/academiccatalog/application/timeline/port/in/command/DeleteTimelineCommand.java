package com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.UUID;

public record DeleteTimelineCommand(UUID timelineId) {
    public DeleteTimelineCommand {
        Preconditions.requireNonNull(timelineId, "timelineId");
    }

    public static DeleteTimelineCommand of(UUID timelineId) {
        return new DeleteTimelineCommand(timelineId);
    }
}
