package com.lilamaris.capstone.timeline.domain.shared;

import com.lilamaris.capstone.kernel.testsupport.FixedClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;

import static com.lilamaris.capstone.timeline.domain.shared.TemporalRangeTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TemporalRange 계약 테스트")
public abstract class AbstractTemporalRangeTest<T extends TemporalRange> {

    Clock clock = FixedClock.getFixed();
    TemporalRange range;

    public abstract T create(Instant startAt, Instant endAt);

    @BeforeEach
    void run() {
        range = create(START_AT, END_AT);
    }

    @Test
    @DisplayName("시작 시간이 종료 시간보다 같거나 이후면 예외")
    void throw_exception_when_startAt_is_after_endAt() {
        assertThatThrownBy(() -> create(START_AT, START_AT))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> create(START_AT, BEFORE_START_AT))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("시작 시간 이상, 종료 시간 이하는 포함한다")
    void contains_within_range() {
        assertThat(range.contains(create(START_AT, END_AT))).isTrue();
        assertThat(range.contains(create(START_AT, BEFORE_END_AT))).isTrue();
        assertThat(range.contains(create(AFTER_START_AT, END_AT))).isTrue();

        assertThat(range.containsBy(create(START_AT, END_AT))).isTrue();
        assertThat(range.containsBy(create(START_AT, AFTER_END_AT))).isTrue();
        assertThat(range.containsBy(create(BEFORE_START_AT, END_AT))).isTrue();
    }

    @Test
    @DisplayName("시작 시간 이전, 종료 시간 이후는 포함하지 않는다")
    void does_not_contain_outside_range() {
        assertThat(range.contains(create(BEFORE_START_AT, START_AT))).isFalse();
        assertThat(range.contains(create(END_AT, AFTER_END_AT))).isFalse();

        assertThat(range.containsBy(create(BEFORE_START_AT, START_AT))).isFalse();
        assertThat(range.containsBy(create(END_AT, AFTER_END_AT))).isFalse();
    }

    @Test
    @DisplayName("Instant 포함 여부는 시작 경계는 포함하고, 종료 경계는 포함하지 않는다")
    void contains_instant_start_boundary_but_not_end_boundary() {
        assertThat(range.contains(START_AT)).isTrue();
        assertThat(range.contains(END_AT)).isFalse();
        assertThat(range.contains(BEFORE_START_AT)).isFalse();
        assertThat(range.contains(AFTER_END_AT)).isFalse();
    }

    @Test
    @DisplayName("두 구간이 겹치면 True")
    void overlap_range() {
        assertThat(range.overlaps(create(BEFORE_END_AT, AFTER_END_AT))).isTrue();
        assertThat(range.overlaps(create(BEFORE_START_AT, AFTER_START_AT))).isTrue();
    }

    @Test
    @DisplayName("두 구간이 겹치지 않으면 False")
    void not_overlap_range() {
        assertThat(range.overlaps(create(BEFORE_START_AT, START_AT))).isFalse();
        assertThat(range.overlaps(create(END_AT, AFTER_END_AT))).isFalse();
    }

    @Test
    @DisplayName("시작 시간과 종료 시간이 같으면 같은 구간")
    void same_range() {
        TemporalRange other = new TemporalRange() {
            @Override
            public Instant startAt() {
                return range.startAt();
            }

            @Override
            public Instant endAt() {
                return range.endAt();
            }
        };

        assertThat(range.isSame(other)).isTrue();
    }

    @Test
    @DisplayName("시작 시간과 종료 시간이 다르면 다른 구간")
    void not_same_range() {
        TemporalRange other = new TemporalRange() {
            @Override
            public Instant startAt() {
                return BEFORE_START_AT;
            }

            @Override
            public Instant endAt() {
                return AFTER_END_AT;
            }
        };

        assertThat(range.isSame(other)).isFalse();
    }
}
