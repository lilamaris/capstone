package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.lilamaris.capstone.academiccatalog.domain.shared.time.TemporalRangeTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TemporalRange 계약 테스트")
public abstract class AbstractTemporalRangeTest<T extends TemporalRange> extends AbstractRangeComparableTest<T> {

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
    @DisplayName("Instant 포함 여부는 시작 경계는 포함하고, 종료 경계는 포함하지 않는다")
    void contains_instant_start_boundary_but_not_end_boundary() {
        assertThat(range.contains(START_AT)).isTrue();
        assertThat(range.contains(END_AT)).isFalse();
        assertThat(range.contains(BEFORE_START_AT)).isFalse();
        assertThat(range.contains(AFTER_END_AT)).isFalse();
    }

    @Override
    protected T createSameRange() {
        return create(START_AT, END_AT);
    }

    @Override
    protected T createContainedRange() {
        return create(AFTER_START_AT, BEFORE_END_AT);
    }

    @Override
    protected T createContainingRange() {
        return create(BEFORE_START_AT, AFTER_END_AT);
    }

    @Override
    protected T createBeforeRange() {
        return create(BEFORE_START_AT, START_AT);
    }

    @Override
    protected T createAfterRange() {
        return create(END_AT, AFTER_END_AT);
    }

    @Override
    protected T createOverlapsBeforeRange() {
        return create(BEFORE_START_AT, AFTER_START_AT);
    }

    @Override
    protected T createOverlapsAfterRange() {
        return create(BEFORE_END_AT, AFTER_END_AT);
    }
}
