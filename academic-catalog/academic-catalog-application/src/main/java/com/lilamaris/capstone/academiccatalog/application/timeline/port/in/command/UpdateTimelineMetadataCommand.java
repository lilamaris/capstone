package com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.util.UUID;

public record UpdateTimelineMetadataCommand(
        UUID timelineId,
        String title,
        String description
) {
    public UpdateTimelineMetadataCommand {
        Preconditions.requireNonNull(timelineId, "timelineId");
        Preconditions.requireNonBlank(title, "title");
        Preconditions.requireNonBlank(description, "description");
    }

    public static UpdateTimelineMetadataCommand of(UUID timelineId, String title, String description) {
        return new UpdateTimelineMetadataCommand(timelineId, title, description);
    }
}
