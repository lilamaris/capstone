package com.lilamaris.capstone.academiccatalog.application.timeline.port.in;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command.CreateTimelineCommand;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.TimelineResult;

public interface CreateTimelineUseCase {
    TimelineResult create(CreateTimelineCommand command);
}
