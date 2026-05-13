package com.lilamaris.capstone.academiccatalog.application.timeline;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.ListTimelineUseCase;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.in.result.PagedResult;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineReader;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineStore;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.criteria.TimelineSearchCriteria;
import com.lilamaris.capstone.academiccatalog.application.timeline.service.TimelineQueryService;
import com.lilamaris.capstone.academiccatalog.domain.timeline.Timeline;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@DisplayName("ListTimelineUseCase 테스트")
class ListTimelineUseCaseTest extends AbstractTimelineUseCaseDISupport<ListTimelineUseCase> {

    @Override
    protected ListTimelineUseCase init(TimelineReader reader, TimelineStore store) {
        return new TimelineQueryService(reader);
    }

    @Nested
    @DisplayName("타임라인 목록 조회")
    class ListTest {
        @Test
        @DisplayName("검색 조건으로 타임라인 목록을 조회한다")
        void find_timeline_list_by_criteria() {
            when(reader.findByCriteria(any(TimelineSearchCriteria.class)))
                    .thenReturn(TimelineUseCaseTestSupport.pagedTimeline());

            var result = useCase.list(TimelineUseCaseTestSupport.listTimelineQuery());

            verify(reader).findByCriteria(any(TimelineSearchCriteria.class));
            assertThat(result.pageNumber()).isEqualTo(TimelineUseCaseTestSupport.PAGE);
            assertThat(result.pageSize()).isEqualTo(TimelineUseCaseTestSupport.SIZE);
            assertThat(result.totalElements()).isEqualTo(TimelineUseCaseTestSupport.TOTAL_ELEMENTS);
            assertThat(result.totalPages()).isEqualTo(TimelineUseCaseTestSupport.TOTAL_PAGES);
            assertThat(result.items())
                    .hasSize(1)
                    .first()
                    .satisfies(item -> {
                        assertThat(item.timelineId()).isEqualTo(TimelineUseCaseTestSupport.TIMELINE_ID);
                        assertThat(item.title()).isEqualTo(TimelineUseCaseTestSupport.TITLE);
                        assertThat(item.description()).isEqualTo(TimelineUseCaseTestSupport.DESCRIPTION);
                        assertThat(item.createdAt()).isEqualTo(TimelineUseCaseTestSupport.NOW);
                    });
        }

        @Test
        @DisplayName("빈 페이지 결과를 조회한다")
        void find_empty_timeline_page() {
            when(reader.findByCriteria(any(TimelineSearchCriteria.class)))
                    .thenReturn(PagedResult.of(TimelineUseCaseTestSupport.PAGE, TimelineUseCaseTestSupport.SIZE, 0, 0, List.<Timeline>of()));

            var result = useCase.list(TimelineUseCaseTestSupport.listTimelineQuery());

            verify(reader).findByCriteria(any(TimelineSearchCriteria.class));
            assertThat(result.totalElements()).isZero();
            assertThat(result.totalPages()).isZero();
            assertThat(result.items()).isEmpty();
        }
    }
}
