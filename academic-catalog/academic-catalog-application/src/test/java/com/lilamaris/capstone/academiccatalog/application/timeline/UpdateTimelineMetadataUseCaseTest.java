package com.lilamaris.capstone.academiccatalog.application.timeline;

import com.lilamaris.capstone.academiccatalog.application.shared.exception.AcademicCatalogApplicationErrorCode;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.UpdateTimelineMetadataUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineReader;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineStore;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.service.TimelineCommandService;
import com.lilamaris.capstone.academiccatalog.domain.timeline.Timeline;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.lilamaris.capstone.kernel.testsupport.assertion.ApplicationAssertions.assertThatApplicationThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@DisplayName("UpdateTimelineMetadataUseCase 테스트")
class UpdateTimelineMetadataUseCaseTest extends AbstractTimelineUseCaseDISupport<UpdateTimelineMetadataUseCase> {

    @Override
    protected UpdateTimelineMetadataUseCase init(TimelineReader reader, TimelineStore store) {
        return new TimelineCommandService(reader, store, clock);
    }

    @Nested
    @DisplayName("타임라인 메타데이터 수정")
    class UpdateTest {
        @Test
        @DisplayName("타임라인을 조회하고 수정된 타임라인을 저장한다")
        void find_timeline_and_save_updated_timeline() {
            when(reader.findById(TimelineUseCaseTestSupport.TIMELINE_ID))
                    .thenReturn(Optional.of(TimelineUseCaseTestSupport.savedTimeline()));
            doAnswer(invocation -> invocation.getArgument(0, Timeline.class))
                    .when(store).save(any(Timeline.class));

            var result = useCase.update(TimelineUseCaseTestSupport.updateTimelineMetadataCommand());

            verify(reader).findById(TimelineUseCaseTestSupport.TIMELINE_ID);
            verify(store).save(any(Timeline.class));
            assertThat(result.timelineId()).isEqualTo(TimelineUseCaseTestSupport.TIMELINE_ID);
            assertThat(result.title()).isEqualTo(TimelineUseCaseTestSupport.UPDATED_TITLE);
            assertThat(result.description()).isEqualTo(TimelineUseCaseTestSupport.UPDATED_DESCRIPTION);
            assertThat(result.createdAt()).isEqualTo(TimelineUseCaseTestSupport.NOW);
        }

        @Test
        @DisplayName("수정할 타임라인이 없으면 예외")
        void throw_exception_when_timeline_not_found() {
            when(reader.findById(TimelineUseCaseTestSupport.TIMELINE_ID)).thenReturn(Optional.empty());

            assertThatApplicationThrownBy(() -> useCase.update(TimelineUseCaseTestSupport.updateTimelineMetadataCommand()))
                    .hasErrorCode(AcademicCatalogApplicationErrorCode.TIMELINE_NOT_FOUND);
        }
    }
}
