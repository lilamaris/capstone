package com.lilamaris.capstone.academiccatalog.application.timeline;

import com.lilamaris.capstone.academiccatalog.application.shared.exception.AcademicCatalogApplicationErrorCode;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.DeleteTimelineUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineReader;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineStore;
import com.lilamaris.capstone.academiccatalog.application.timeline.service.TimelineCommandService;
import com.lilamaris.capstone.academiccatalog.domain.timeline.Timeline;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.lilamaris.capstone.kernel.testsupport.assertion.ApplicationAssertions.assertThatApplicationThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@DisplayName("DeleteTimelineUseCase 테스트")
class DeleteTimelineUseCaseTest extends AbstractTimelineUseCaseDISupport<DeleteTimelineUseCase> {

    @Override
    protected DeleteTimelineUseCase init(TimelineReader reader, TimelineStore store) {
        return new TimelineCommandService(reader, store, clock);
    }

    @Nested
    @DisplayName("타임라인 삭제")
    class DeleteTest {
        @Test
        @DisplayName("타임라인을 조회하고 삭제한다")
        void find_timeline_and_delete() {
            when(reader.findById(TimelineUseCaseTestSupport.TIMELINE_ID))
                    .thenReturn(Optional.of(TimelineUseCaseTestSupport.savedTimeline()));

            useCase.delete(TimelineUseCaseTestSupport.deleteTimelineCommand());

            verify(reader).findById(TimelineUseCaseTestSupport.TIMELINE_ID);
            verify(store).delete(any(Timeline.class));
        }

        @Test
        @DisplayName("삭제할 타임라인이 없으면 예외")
        void throw_exception_when_timeline_not_found() {
            when(reader.findById(TimelineUseCaseTestSupport.TIMELINE_ID)).thenReturn(Optional.empty());

            assertThatApplicationThrownBy(() -> useCase.delete(TimelineUseCaseTestSupport.deleteTimelineCommand()))
                    .hasErrorCode(AcademicCatalogApplicationErrorCode.TIMELINE_NOT_FOUND);
        }
    }
}
