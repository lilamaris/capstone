package com.lilamaris.capstone.academiccatalog.application.timeline;

import com.lilamaris.capstone.academiccatalog.application.shared.exception.AcademicCatalogApplicationErrorCode;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.GetTimelineUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineReader;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineStore;
import com.lilamaris.capstone.academiccatalog.application.timeline.service.TimelineQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.lilamaris.capstone.kernel.testsupport.assertion.ApplicationAssertions.assertThatApplicationThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@DisplayName("GetTimelineUseCase 테스트")
class GetTimelineUseCaseTest extends AbstractTimelineUseCaseDISupport<GetTimelineUseCase> {

    @Override
    protected GetTimelineUseCase init(TimelineReader reader, TimelineStore store) {
        return new TimelineQueryService(reader);
    }

    @Nested
    @DisplayName("타임라인 단건 조회")
    class GetTest {
        @Test
        @DisplayName("타임라인을 조회한다")
        void find_timeline() {
            when(reader.findById(TimelineUseCaseTestSupport.TIMELINE_ID))
                    .thenReturn(Optional.of(TimelineUseCaseTestSupport.savedTimeline()));

            var result = useCase.get(TimelineUseCaseTestSupport.getTimelineQuery());

            verify(reader).findById(TimelineUseCaseTestSupport.TIMELINE_ID);
            assertThat(result.timelineId()).isEqualTo(TimelineUseCaseTestSupport.TIMELINE_ID);
            assertThat(result.title()).isEqualTo(TimelineUseCaseTestSupport.TITLE);
            assertThat(result.description()).isEqualTo(TimelineUseCaseTestSupport.DESCRIPTION);
            assertThat(result.createdAt()).isEqualTo(TimelineUseCaseTestSupport.NOW);
        }

        @Test
        @DisplayName("조회할 타임라인이 없으면 예외")
        void throw_exception_when_timeline_not_found() {
            when(reader.findById(TimelineUseCaseTestSupport.TIMELINE_ID)).thenReturn(Optional.empty());

            assertThatApplicationThrownBy(() -> useCase.get(TimelineUseCaseTestSupport.getTimelineQuery()))
                    .hasErrorCode(AcademicCatalogApplicationErrorCode.TIMELINE_NOT_FOUND);
        }
    }
}
