package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.lilamaris.capstone.academiccatalog.domain.shared.time.DailyScheduleTestSupport.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SimpleDailySchedule 테스트")
class SimpleDailyScheduleTest extends AbstractDailyScheduleTest<SimpleDailySchedule> {
    @Override
    protected SimpleDailySchedule create(List<DailyNanoRange> ranges) {
        return SimpleDailySchedule.of(ranges);
    }

    @Test
    @DisplayName("DailySchedule로부터 복사할 수 있다")
    void copy_from_daily_schedule() {
        var schedule = SimpleDailySchedule.from(create(List.of(
                range("09:00", "12:00"),
                range("13:00", "18:00")
        )));

        assertThat(schedule.ranges())
                .extracting(DailyNanoRange::start)
                .containsExactly(
                        DailyScheduleTestSupport.nanoOf("09:00"),
                        DailyScheduleTestSupport.nanoOf("13:00")
                );

        assertThatThrownBy(() -> SimpleDailySchedule.from(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("schedule must not be null.");
    }
}
