package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.lilamaris.capstone.academiccatalog.domain.shared.time.DailyNanoRangeTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("EmbeddableDailyNanoRange 테스트")
public class EmbeddableDailyNanoRangeTest extends AbstractDailyNanoRangeTest<EmbeddableDailyNanoRange> {
    @Override
    public EmbeddableDailyNanoRange create(long startNanoOfDay, long endNanoOfDay) {
        return EmbeddableDailyNanoRange.of(startNanoOfDay, endNanoOfDay);
    }

    @Test
    @DisplayName("시작 나노와 Duration으로 생성할 수 있다")
    void create_with_start_nano_of_day_and_duration() {
        var range = EmbeddableDailyNanoRange.of(START_NANO_OF_DAY, DURATION);

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY);
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY);
    }

    @Test
    @DisplayName("Duration이 양수가 아니면 예외")
    void throw_exception_when_duration_is_not_positive() {
        assertThatThrownBy(() -> EmbeddableDailyNanoRange.of(START_NANO_OF_DAY, Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be positive.");

        assertThatThrownBy(() -> EmbeddableDailyNanoRange.of(START_NANO_OF_DAY, Duration.ofNanos(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be positive.");
    }

    @Test
    @DisplayName("DailyNanoRange로부터 복사할 수 있다")
    void copy_from_daily_nano_range() {
        var range = EmbeddableDailyNanoRange.from(create(START_NANO_OF_DAY, END_NANO_OF_DAY));

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY);
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY);

        assertThatThrownBy(() -> EmbeddableDailyNanoRange.from(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("range must not be null.");
    }

    @Test
    @DisplayName("시작 나노를 변경할 수 있다")
    void update_start_nano_of_day() {
        var range = create(START_NANO_OF_DAY, END_NANO_OF_DAY);

        range.updateStartNanoOfDay(START_NANO_OF_DAY + Duration.ofHours(1).toNanos());

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY + Duration.ofHours(1).toNanos());
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY);
    }

    @Test
    @DisplayName("종료 나노를 변경할 수 있다")
    void update_end_nano_of_day() {
        var range = create(START_NANO_OF_DAY, END_NANO_OF_DAY);

        range.updateEndNanoOfDay(END_NANO_OF_DAY - Duration.ofHours(1).toNanos());

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY);
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY - Duration.ofHours(1).toNanos());
    }

    @Test
    @DisplayName("시작 나노를 확장할 수 있다")
    void extend_start_nano_of_day() {
        var range = create(START_NANO_OF_DAY, END_NANO_OF_DAY);

        range.extendStartNanoOfDay(Duration.ofHours(-1));

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY - Duration.ofHours(1).toNanos());
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY);
    }

    @Test
    @DisplayName("종료 나노를 확장할 수 있다")
    void extend_end_nano_of_day() {
        var range = create(START_NANO_OF_DAY, END_NANO_OF_DAY);

        range.extendEndNanoOfDay(Duration.ofHours(1));

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY);
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY + Duration.ofHours(1).toNanos());
    }

    @Test
    @DisplayName("시간 구간 전체를 이동할 수 있다")
    void adjust_offset() {
        var range = create(START_NANO_OF_DAY, END_NANO_OF_DAY);

        range.adjustOffset(Duration.ofHours(1));

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY + Duration.ofHours(1).toNanos());
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY + Duration.ofHours(1).toNanos());
    }
}
