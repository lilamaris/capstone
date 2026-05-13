package com.lilamaris.capstone.academiccatalog.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static com.lilamaris.capstone.academiccatalog.domain.shared.DailyScheduleTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("DailySchedule 계약 테스트")
public abstract class AbstractDailyScheduleTest<T extends DailySchedule> {

    protected abstract T create(List<DailyNanoRange> ranges);

    @Test
    @DisplayName("DailyNanoRange 목록을 시작 나노 기준으로 정렬할 수 있다")
    void sort_ranges_by_start_nano_of_day() {
        var schedule = create(List.of(
                range("18:00", "20:00"),
                range("09:00", "12:00"),
                range("13:00", "17:00")
        ));

        assertThat(schedule.ranges())
                .extracting(DailyNanoRange::startNanoOfDay)
                .containsExactly(
                        nanoOf("09:00"),
                        nanoOf("13:00"),
                        nanoOf("18:00")
                );
    }

    @Test
    @DisplayName("DailyNanoRange 목록이 비어있으면 예외")
    void throw_exception_when_ranges_is_empty() {
        assertThatThrownBy(() -> create(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ranges must not be empty.");
    }

    @Test
    @DisplayName("DailyNanoRange 목록에 null이 있으면 예외")
    void throw_exception_when_range_is_null() {
        assertThatThrownBy(() -> create(Arrays.asList(range("09:00", "12:00"), null)))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("range must not be null.");
    }

    @Test
    @DisplayName("DailyNanoRange 목록에 겹치는 구간이 있으면 예외")
    void throw_exception_when_ranges_overlap() {
        assertThatThrownBy(() -> create(List.of(
                range("09:00", "12:00"),
                range("11:00", "13:00")
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ranges must not overlap.");
    }

    @Test
    @DisplayName("DailyNanoRange 목록에 인접한 구간이 있으면 허용한다")
    void allow_adjacent_ranges() {
        var schedule = create(List.of(
                range("09:00", "12:00"),
                range("12:00", "18:00")
        ));

        assertThat(schedule.ranges()).hasSize(2);
    }

    @Test
    @DisplayName("DailyNanoRange 목록은 값으로 복사한다")
    void copy_ranges_by_value() {
        var mutable = EmbeddableDailyNanoRange.of(nanoOf("09:00"), nanoOf("12:00"));
        var schedule = create(List.of(mutable));

        mutable.updateStartNanoOfDay(nanoOf("10:00"));

        assertThat(schedule.ranges().getFirst().startNanoOfDay()).isEqualTo(nanoOf("09:00"));
        assertThat(schedule.ranges().getFirst().endNanoOfDay()).isEqualTo(nanoOf("12:00"));
    }

    @Test
    @DisplayName("nanoOfDay 포함 여부를 확인할 수 있다")
    void contains_nano_of_day() {
        var schedule = create(List.of(
                range("09:00", "12:00"),
                range("13:00", "18:00")
        ));

        assertThat(schedule.contains(nanoOf("09:00"))).isTrue();
        assertThat(schedule.contains(nanoOf("12:00"))).isFalse();
        assertThat(schedule.contains(nanoOf("13:00"))).isTrue();
        assertThat(schedule.contains(nanoOf("18:00"))).isFalse();
    }

    @Test
    @DisplayName("LocalTime 포함 여부를 확인할 수 있다")
    void contains_local_time() {
        var schedule = create(List.of(range("09:00", "18:00")));

        assertThat(schedule.contains(LocalTime.of(9, 0))).isTrue();
        assertThat(schedule.contains(LocalTime.of(18, 0))).isFalse();

        assertThatThrownBy(() -> schedule.contains((LocalTime) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("other must not be null.");
    }

    @Test
    @DisplayName("Instant 포함 여부를 ZoneId 기준으로 확인할 수 있다")
    void contains_instant_with_zone_id() {
        var schedule = create(List.of(range("09:00", "18:00")));

        assertThat(schedule.contains(Instant.parse("2026-01-01T00:00:00Z"), SEOUL)).isTrue();
        assertThat(schedule.contains(Instant.parse("2026-01-01T09:00:00Z"), SEOUL)).isFalse();

        assertThatThrownBy(() -> schedule.contains((Instant) null, SEOUL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("other must not be null.");
        assertThatThrownBy(() -> schedule.contains(Instant.parse("2026-01-01T00:00:00Z"), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("zoneId must not be null.");
    }

    @Test
    @DisplayName("DailyNanoRange가 하나의 구간에 포함되면 포함한다")
    void contains_daily_nano_range_with_single_range() {
        var schedule = create(List.of(range("09:00", "18:00")));

        assertThat(schedule.contains(range("10:00", "17:00"))).isTrue();
        assertThat(schedule.contains(range("08:00", "17:00"))).isFalse();
        assertThat(schedule.contains(range("10:00", "19:00"))).isFalse();
    }

    @Test
    @DisplayName("DailyNanoRange가 인접한 여러 구간으로 덮이면 포함한다")
    void contains_daily_nano_range_with_adjacent_ranges() {
        var schedule = create(List.of(
                range("09:00", "12:00"),
                range("12:00", "18:00")
        ));

        assertThat(schedule.contains(range("09:00", "18:00"))).isTrue();
    }

    @Test
    @DisplayName("DailyNanoRange 사이에 빈 구간이 있으면 포함하지 않는다")
    void does_not_contain_daily_nano_range_when_gap_exists() {
        var schedule = create(List.of(
                range("09:00", "12:00"),
                range("13:00", "18:00")
        ));

        assertThat(schedule.contains(range("09:00", "18:00"))).isFalse();
        assertThatThrownBy(() -> schedule.contains((DailyNanoRange) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("other must not be null.");
    }

    @Test
    @DisplayName("하루 전체를 포함하면 always schedule이다")
    void always_schedule() {
        assertThat(create(List.of(range("00:00", DailyNanoRange.DAY_NANOS))).isAlways()).isTrue();
        assertThat(create(List.of(range("00:00", "23:59"))).isAlways()).isFalse();
    }

    @Test
    @DisplayName("TemporalRange가 하루 안에서 DailySchedule에 포함되면 포함한다")
    void contains_temporal_range_within_day() {
        var schedule = create(List.of(range("09:00", "18:00")));

        assertThat(schedule.contains(withinDayTemporalRange(), SEOUL)).isTrue();
    }

    @Test
    @DisplayName("TemporalRange가 자정을 넘어도 날짜별 구간이 모두 포함되면 포함한다")
    void contains_temporal_range_crossing_midnight() {
        var schedule = create(List.of(
                range("00:00", "02:00"),
                range("22:00", DailyNanoRange.DAY_NANOS)
        ));

        assertThat(schedule.contains(crossingMidnightTemporalRange(), SEOUL)).isTrue();
    }

    @Test
    @DisplayName("TemporalRange가 자정을 넘을 때 날짜별 구간 중 하나라도 비어있으면 포함하지 않는다")
    void does_not_contain_temporal_range_crossing_midnight_when_gap_exists() {
        var schedule = create(List.of(range("22:00", DailyNanoRange.DAY_NANOS)));

        assertThat(schedule.contains(crossingMidnightTemporalRange(), SEOUL)).isFalse();
    }

    @Test
    @DisplayName("TemporalRange가 24시간 이상이어도 하루 전체 스케줄이면 포함한다")
    void contains_temporal_range_longer_than_day_when_always() {
        var schedule = create(List.of(range("00:00", DailyNanoRange.DAY_NANOS)));

        assertThat(schedule.contains(longerThanDayTemporalRange(), UTC)).isTrue();
    }

    @Test
    @DisplayName("TemporalRange가 24시간 이상이면 각 날짜별 구간을 모두 검사한다")
    void contains_temporal_range_longer_than_day_checks_every_daily_chunk() {
        var schedule = create(List.of(
                range("00:00", "12:00"),
                range("13:00", DailyNanoRange.DAY_NANOS)
        ));

        assertThat(schedule.contains(longChunkCheckedTemporalRange(), UTC)).isFalse();
    }

    @Test
    @DisplayName("TemporalRange가 정확히 자정에 끝나면 종료 경계를 하루 끝으로 취급한다")
    void contains_temporal_range_ending_at_midnight() {
        var schedule = create(List.of(range("22:00", DailyNanoRange.DAY_NANOS)));

        assertThat(schedule.contains(endingAtMidnightTemporalRange(), SEOUL)).isTrue();
    }

    @Test
    @DisplayName("TemporalRange가 여러 날짜에 걸치면 모든 날짜별 조각을 검사한다")
    void contains_temporal_range_checks_all_daily_chunks() {
        var schedule = create(List.of(
                range("00:00", "02:00"),
                range("22:00", DailyNanoRange.DAY_NANOS)
        ));

        assertThat(schedule.contains(multiDayChunkTemporalRange(), UTC)).isFalse();
    }

    @Test
    @DisplayName("TemporalRange 포함 여부를 DST 전환일에도 local date boundary 기준으로 확인한다")
    void contains_temporal_range_on_dst_transition_day() {
        var schedule = create(List.of(range("00:00", DailyNanoRange.DAY_NANOS)));

        assertThat(schedule.contains(dstTransitionTemporalRange(), NEW_YORK)).isTrue();
    }

    @Test
    @DisplayName("TemporalRange 포함 여부 검증 대상이 null이면 예외")
    void throw_exception_when_temporal_range_or_zone_id_is_null() {
        var schedule = create(List.of(range("00:00", DailyNanoRange.DAY_NANOS)));
        var range = SimpleTemporalRange.of(
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-01T01:00:00Z")
        );

        assertThatThrownBy(() -> schedule.contains((TemporalRange) null, UTC))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("other must not be null.");
        assertThatThrownBy(() -> schedule.contains(range, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("zoneId must not be null.");
    }

    @Test
    @DisplayName("하나 이상의 구간이 대상 구간과 겹치면 true")
    void return_true_when_any_range_overlaps_target_range() {
        var ranges = create(List.of(
                range("09:00", "12:00"),
                range("13:00", "18:00")
        ));

        assertThat(ranges.overlaps(range("12:00", DailyNanoRange.DAY_NANOS))).isTrue();
    }

    @Test
    @DisplayName("어떤 구간도 대상 구간과 겹치지 않으면 false")
    void return_false_when_no_range_overlaps_target_range() {
        var ranges = create(List.of(
                range("09:00", "12:00"),
                range("13:00", "18:00")
        ));

        assertThat(ranges.overlaps(range("12:00", "13:00"))).isFalse();
    }

    @Test
    @DisplayName("하나 이상의 구간이 다른 DailyNanoRange와 겹치면 true")
    void return_true_when_any_range_overlaps_other_ranges() {
        var ranges = create(List.of(
                range("09:00", "12:00"),
                range("13:00", "18:00")
        ));

        var other = create(List.of(
                range("12:00", DailyNanoRange.DAY_NANOS)
        ));

        assertThat(ranges.overlaps(other)).isTrue();
    }

    @Test
    @DisplayName("어떤 구간도 다른 DailyNanoRange와 겹치지 않으면 false")
    void return_false_when_no_range_overlaps_other_ranges() {
        var ranges = create(List.of(
                range("09:00", "12:00"),
                range("13:00", "18:00")
        ));

        var other = create(List.of(
                range("12:00", "13:00")
        ));

        assertThat(ranges.overlaps(other)).isFalse();
    }

    @Test
    @DisplayName("겹침 여부 조회 인자가 null이면 예외")
    void throw_exception_when_overlaps_argument_is_null() {
        var ranges = create(List.of(
                range("09:00", "12:00"),
                range("13:00", "18:00")
        ));

        assertThatThrownBy(() -> ranges.overlaps((DailyNanoRange) null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ranges.overlaps((DailyNanoRange) null))
                .isInstanceOf(NullPointerException.class);
    }
}
