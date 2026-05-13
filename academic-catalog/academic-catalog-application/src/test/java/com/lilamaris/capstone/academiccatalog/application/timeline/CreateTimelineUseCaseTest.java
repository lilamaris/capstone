package com.lilamaris.capstone.academiccatalog.application.timeline;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.CreateTimelineUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineReader;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineStore;
import com.lilamaris.capstone.academiccatalog.application.timeline.service.TimelineCommandService;
import com.lilamaris.capstone.academiccatalog.domain.timeline.Timeline;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@Tag("unit")
@DisplayName("CreateTimelineUseCase 테스트")
class CreateTimelineUseCaseTest extends AbstractTimelineUseCaseDISupport<CreateTimelineUseCase> {

    @Override
    protected CreateTimelineUseCase init(TimelineReader reader, TimelineStore store) {
        return new TimelineCommandService(reader, store, clock);
    }

    @Nested
    @DisplayName("타임라인 생성")
    class CreateTest {
        @Test
        @DisplayName("타임라인을 저장한다")
        void save_timeline() {
            doAnswer(invocation -> {
                var timeline = invocation.getArgument(0, Timeline.class);
                TimelineUseCaseTestSupport.assignId(timeline, TimelineUseCaseTestSupport.TIMELINE_ID);
                return timeline;
            }).when(store).save(any(Timeline.class));

            var result = useCase.create(TimelineUseCaseTestSupport.createTimelineCommand());

            verify(store).save(any(Timeline.class));
            assertThat(result.timelineId()).isEqualTo(TimelineUseCaseTestSupport.TIMELINE_ID);
            assertThat(result.title()).isEqualTo(TimelineUseCaseTestSupport.TITLE);
            assertThat(result.description()).isEqualTo(TimelineUseCaseTestSupport.DESCRIPTION);
            assertThat(result.createdAt()).isEqualTo(TimelineUseCaseTestSupport.NOW);
        }

        @Test
        @DisplayName("제목이 비어 있으면 예외")
        void throw_exception_when_title_is_blank() {
            assertThatDomainThrownBy(() -> useCase.create(
                    TimelineUseCaseTestSupport.createTimelineCommand("", TimelineUseCaseTestSupport.DESCRIPTION)
            ))
                    .hasNonBlankMessageFor("title");
        }
    }
}
