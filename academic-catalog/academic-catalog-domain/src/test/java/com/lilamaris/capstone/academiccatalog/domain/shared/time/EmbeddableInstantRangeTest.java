package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static com.lilamaris.capstone.academiccatalog.domain.shared.time.InstantRangeTestSupport.END_AT;
import static com.lilamaris.capstone.academiccatalog.domain.shared.time.InstantRangeTestSupport.START_AT;
import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddableInstantRangeTest extends AbstractInstantRangeTest<EmbeddableInstantRange> {
    @Override
    public EmbeddableInstantRange create(Instant startAt, Instant endAt) {
        return EmbeddableInstantRange.of(startAt, endAt);
    }

    @Test
    @DisplayName("InstantRange로부터 복사할 수 있다")
    void copy_from_temporal_range() {
        var range = EmbeddableInstantRange.from(create(START_AT, END_AT));

        assertThat(range.start()).isEqualTo(START_AT);
        assertThat(range.end()).isEqualTo(END_AT);

        assertThatDomainThrownBy(() -> EmbeddableInstantRange.from(null))
                .hasNonNullMessageFor("range");
    }

    @Test
    @DisplayName("시작 시간을 변경할 수 있다")
    void update_start_at() {
        var range = create(START_AT, END_AT);
        var newStartAt = START_AT.plusSeconds(1);

        range.updateStartAt(newStartAt);

        assertThat(range.start()).isEqualTo(newStartAt);
        assertThat(range.end()).isEqualTo(END_AT);
    }

    @Test
    @DisplayName("종료 시간을 변경할 수 있다")
    void update_end_at() {
        var range = create(START_AT, END_AT);
        var newEndAt = END_AT.minusSeconds(1);

        range.updateEndAt(newEndAt);

        assertThat(range.start()).isEqualTo(START_AT);
        assertThat(range.end()).isEqualTo(newEndAt);
    }

    @Test
    @DisplayName("시작 시간을 확장할 수 있다")
    void extend_start_at() {
        var range = create(START_AT, END_AT);

        range.extendStartAt(Duration.ofSeconds(-1));

        assertThat(range.start()).isEqualTo(START_AT.minusSeconds(1));
        assertThat(range.end()).isEqualTo(END_AT);
    }

    @Test
    @DisplayName("종료 시간을 확장할 수 있다")
    void extend_end_at() {
        var range = create(START_AT, END_AT);

        range.extendEndAt(Duration.ofSeconds(1));

        assertThat(range.start()).isEqualTo(START_AT);
        assertThat(range.end()).isEqualTo(END_AT.plusSeconds(1));
    }

    @Test
    @DisplayName("시간 구간 전체를 이동할 수 있다")
    void adjust_offset() {
        var range = create(START_AT, END_AT);

        range.adjustOffset(Duration.ofSeconds(1));

        assertThat(range.start()).isEqualTo(START_AT.plusSeconds(1));
        assertThat(range.end()).isEqualTo(END_AT.plusSeconds(1));
    }
}
