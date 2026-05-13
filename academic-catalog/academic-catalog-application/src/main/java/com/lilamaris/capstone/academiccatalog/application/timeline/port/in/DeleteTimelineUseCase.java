package com.lilamaris.capstone.academiccatalog.application.timeline.port.in;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command.DeleteTimelineCommand;

public interface DeleteTimelineUseCase {
    void delete(DeleteTimelineCommand command);
}
