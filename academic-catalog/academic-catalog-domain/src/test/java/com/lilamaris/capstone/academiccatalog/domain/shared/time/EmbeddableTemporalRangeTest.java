package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static com.lilamaris.capstone.academiccatalog.domain.shared.time.TemporalRangeTestSupport.END_AT;
import static com.lilamaris.capstone.academiccatalog.domain.shared.time.TemporalRangeTestSupport.START_AT;
import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddableTemporalRangeTest extends AbstractTemporalRangeTest<EmbeddableTemporalRange> {
    @Override
    public EmbeddableTemporalRange create(Instant startAt, Instant endAt) {
        return EmbeddableTemporalRange.of(startAt, endAt);
    }

    @Test
    @DisplayName("TemporalRange로부터 복사할 수 있다")
    void copy_from_temporal_range() {
        var range = EmbeddableTemporalRange.from(create(START_AT, END_AT));

        assertThat(range.startAt()).isEqualTo(START_AT);
        assertThat(range.endAt()).isEqualTo(END_AT);

        assertThatDomainThrownBy(() -> EmbeddableTemporalRange.from(null))
                .hasNonNullMessageFor("range");
    }

    @Test
    @DisplayName("시작 시간을 변경할 수 있다")
    void update_start_at() {
        var range = create(START_AT, END_AT);
        var newStartAt = START_AT.plusSeconds(1);

        range.updateStartAt(newStartAt);

        assertThat(range.startAt()).isEqualTo(newStartAt);
        assertThat(range.endAt()).isEqualTo(END_AT);
    }

    @Test
    @DisplayName("종료 시간을 변경할 수 있다")
    void update_end_at() {
        var range = create(START_AT, END_AT);
        var newEndAt = END_AT.minusSeconds(1);

        range.updateEndAt(newEndAt);

        assertThat(range.startAt()).isEqualTo(START_AT);
        assertThat(range.endAt()).isEqualTo(newEndAt);
    }

    @Test
    @DisplayName("시작 시간을 확장할 수 있다")
    void extend_start_at() {
        var range = create(START_AT, END_AT);

        range.extendStartAt(Duration.ofSeconds(-1));

        assertThat(range.startAt()).isEqualTo(START_AT.minusSeconds(1));
        assertThat(range.endAt()).isEqualTo(END_AT);
    }

    @Test
    @DisplayName("종료 시간을 확장할 수 있다")
    void extend_end_at() {
        var range = create(START_AT, END_AT);

        range.extendEndAt(Duration.ofSeconds(1));

        assertThat(range.startAt()).isEqualTo(START_AT);
        assertThat(range.endAt()).isEqualTo(END_AT.plusSeconds(1));
    }

    @Test
    @DisplayName("시간 구간 전체를 이동할 수 있다")
    void adjust_offset() {
        var range = create(START_AT, END_AT);

        range.adjustOffset(Duration.ofSeconds(1));

        assertThat(range.startAt()).isEqualTo(START_AT.plusSeconds(1));
        assertThat(range.endAt()).isEqualTo(END_AT.plusSeconds(1));
    }
}
