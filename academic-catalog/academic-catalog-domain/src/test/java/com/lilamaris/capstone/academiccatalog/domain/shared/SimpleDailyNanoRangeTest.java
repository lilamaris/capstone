package com.lilamaris.capstone.academiccatalog.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;

import static com.lilamaris.capstone.academiccatalog.domain.shared.DailyNanoRangeTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SimpleDailyNanoRange 테스트")
public class SimpleDailyNanoRangeTest extends AbstractDailyNanoRangeTest<SimpleDailyNanoRange> {
    @Override
    public SimpleDailyNanoRange create(long startNanoOfDay, long endNanoOfDay) {
        return SimpleDailyNanoRange.of(startNanoOfDay, endNanoOfDay);
    }

    @Test
    @DisplayName("시작 나노와 Duration으로 생성할 수 있다")
    void create_with_start_nano_of_day_and_duration() {
        var range = SimpleDailyNanoRange.of(START_NANO_OF_DAY, DURATION);

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY);
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY);
    }

    @Test
    @DisplayName("시작 시간과 Duration으로 생성할 수 있다")
    void create_with_start_at_and_duration() {
        var range = SimpleDailyNanoRange.of(LocalTime.ofNanoOfDay(START_NANO_OF_DAY), DURATION);

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY);
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY);
    }

    @Test
    @DisplayName("Duration이 양수가 아니면 예외")
    void throw_exception_when_duration_is_not_positive() {
        assertThatThrownBy(() -> SimpleDailyNanoRange.of(START_NANO_OF_DAY, Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be positive.");

        assertThatThrownBy(() -> SimpleDailyNanoRange.of(START_NANO_OF_DAY, Duration.ofNanos(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be positive.");
    }

    @Test
    @DisplayName("DailyNanoRange로부터 복사할 수 있다")
    void copy_from_daily_nano_range() {
        var range = SimpleDailyNanoRange.from(create(START_NANO_OF_DAY, END_NANO_OF_DAY));

        assertThat(range.startNanoOfDay()).isEqualTo(START_NANO_OF_DAY);
        assertThat(range.endNanoOfDay()).isEqualTo(END_NANO_OF_DAY);

        assertThatThrownBy(() -> SimpleDailyNanoRange.from(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("range must not be null.");
    }
}
