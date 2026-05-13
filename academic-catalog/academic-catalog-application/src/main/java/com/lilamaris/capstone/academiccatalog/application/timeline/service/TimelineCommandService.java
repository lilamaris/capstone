package com.lilamaris.capstone.academiccatalog.application.timeline.service;

import com.lilamaris.capstone.academiccatalog.application.shared.exception.AcademicCatalogApplicationErrorCode;
import com.lilamaris.capstone.academiccatalog.application.shared.exception.AcademicCatalogApplicationException;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.CreateTimelineUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.DeleteTimelineUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.UpdateTimelineMetadataUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command.CreateTimelineCommand;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command.DeleteTimelineCommand;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command.UpdateTimelineMetadataCommand;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.TimelineResult;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineReader;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineStore;
import com.lilamaris.capstone.academiccatalog.domain.timeline.Timeline;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
@Transactional
public class TimelineCommandService implements
        CreateTimelineUseCase,
        UpdateTimelineMetadataUseCase,
        DeleteTimelineUseCase {

    private final TimelineReader reader;
    private final TimelineStore store;

    private final Clock clock;

    @Override
    public TimelineResult create(CreateTimelineCommand command) {
        var now = clock.instant();
        var timeline = Timeline.of(command.title(), command.description(), now);

        var savedTimeline = store.save(timeline);

        return TimelineResult.from(savedTimeline);
    }

    @Override
    public TimelineResult update(UpdateTimelineMetadataCommand command) {
        var timeline = reader.findById(command.timelineId())
                .orElseThrow(() -> new AcademicCatalogApplicationException(AcademicCatalogApplicationErrorCode.TIMELINE_NOT_FOUND));

        timeline.updateTitle(command.title());
        timeline.updateDescription(command.description());

        var savedTimeline = store.save(timeline);

        return TimelineResult.from(savedTimeline);
    }

    @Override
    public void delete(DeleteTimelineCommand command) {
        var timeline = reader.findById(command.timelineId())
                .orElseThrow(() -> new AcademicCatalogApplicationException(AcademicCatalogApplicationErrorCode.TIMELINE_NOT_FOUND));

        store.delete(timeline);
    }
}
