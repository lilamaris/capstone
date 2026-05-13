package com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

public record CreateTimelineCommand(
        String title,
        String description
) {
    public CreateTimelineCommand {
        Preconditions.requireNonBlank(title, "title");
        Preconditions.requireNonBlank(description, "description");
    }

    public static CreateTimelineCommand of(String title, String description) {
        return new CreateTimelineCommand(title, description);
    }
}
