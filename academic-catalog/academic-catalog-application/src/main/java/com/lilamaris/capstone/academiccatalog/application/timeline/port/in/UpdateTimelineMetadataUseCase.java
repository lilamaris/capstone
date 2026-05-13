package com.lilamaris.capstone.academiccatalog.application.timeline.port.in;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command.UpdateTimelineMetadataCommand;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.TimelineResult;

public interface UpdateTimelineMetadataUseCase {
    TimelineResult update(UpdateTimelineMetadataCommand command);
}
