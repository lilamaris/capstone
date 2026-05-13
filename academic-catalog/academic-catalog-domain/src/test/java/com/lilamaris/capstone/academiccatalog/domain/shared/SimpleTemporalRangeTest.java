package com.lilamaris.capstone.academiccatalog.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.lilamaris.capstone.academiccatalog.domain.shared.TemporalRangeTestSupport.END_AT;
import static com.lilamaris.capstone.academiccatalog.domain.shared.TemporalRangeTestSupport.START_AT;
import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SimpleTemporalRange 테스트")
public class SimpleTemporalRangeTest extends AbstractTemporalRangeTest<SimpleTemporalRange> {
    @Override
    public SimpleTemporalRange create(Instant startAt, Instant endAt) {
        return SimpleTemporalRange.of(startAt, endAt);
    }

    @Test
    @DisplayName("TemporalRange로부터 복사할 수 있다")
    void copy_from_temporal_range() {
        var range = SimpleTemporalRange.from(create(START_AT, END_AT));

        assertThat(range.startAt()).isEqualTo(START_AT);
        assertThat(range.endAt()).isEqualTo(END_AT);

        assertThatDomainThrownBy(() -> SimpleTemporalRange.from(null))
                .hasNonNullMessageFor("range");
    }
}
