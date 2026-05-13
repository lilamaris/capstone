package com.lilamaris.capstone.academiccatalog.application.timeline;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command.CreateTimelineCommand;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command.DeleteTimelineCommand;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.command.UpdateTimelineMetadataCommand;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query.GetTimelineQuery;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query.ListTimelineQuery;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.query.PageQuery;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.PagedResult;
import com.lilamaris.capstone.academiccatalog.domain.timeline.Timeline;
import com.lilamaris.capstone.kernel.testsupport.FixedClock;
import com.lilamaris.capstone.kernel.testsupport.generator.SequenceCounter;
import com.lilamaris.capstone.kernel.testsupport.generator.UuidGenerator;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class TimelineUseCaseTestSupport {
    public static final Clock CLOCK = FixedClock.getFixed();
    public static final Instant NOW = CLOCK.instant();

    public static final UUID TIMELINE_ID = new UuidGenerator(new SequenceCounter(1)).generate();

    public static final String TITLE = "2026-1 Academic Timeline";
    public static final String UPDATED_TITLE = "2026-2 Academic Timeline";
    public static final String DESCRIPTION = "Spring semester registration timeline";
    public static final String UPDATED_DESCRIPTION = "Fall semester registration timeline";
    public static final String KEYWORD = "Academic";

    public static final int PAGE = 0;
    public static final int SIZE = 10;
    public static final long TOTAL_ELEMENTS = 1L;
    public static final int TOTAL_PAGES = 1;

    private TimelineUseCaseTestSupport() {
    }

    public static Timeline timeline() {
        return Timeline.of(TITLE, DESCRIPTION, NOW);
    }

    public static Timeline savedTimeline() {
        var timeline = timeline();
        assignId(timeline, TIMELINE_ID);
        return timeline;
    }

    public static CreateTimelineCommand createTimelineCommand() {
        return CreateTimelineCommand.of(TITLE, DESCRIPTION);
    }

    public static CreateTimelineCommand createTimelineCommand(String title, String description) {
        return CreateTimelineCommand.of(title, description);
    }

    public static UpdateTimelineMetadataCommand updateTimelineMetadataCommand() {
        return UpdateTimelineMetadataCommand.of(TIMELINE_ID, UPDATED_TITLE, UPDATED_DESCRIPTION);
    }

    public static DeleteTimelineCommand deleteTimelineCommand() {
        return DeleteTimelineCommand.of(TIMELINE_ID);
    }

    public static GetTimelineQuery getTimelineQuery() {
        return GetTimelineQuery.of(TIMELINE_ID);
    }

    public static ListTimelineQuery listTimelineQuery() {
        return ListTimelineQuery.of(KEYWORD, PageQuery.of(PAGE, SIZE));
    }

    public static PagedResult<Timeline> pagedTimeline() {
        return PagedResult.of(PAGE, SIZE, TOTAL_ELEMENTS, TOTAL_PAGES, List.of(savedTimeline()));
    }

    public static void assignId(Timeline timeline, UUID id) {
        try {
            var field = Timeline.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(timeline, id);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
