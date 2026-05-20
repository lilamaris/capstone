package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.lilamaris.capstone.academiccatalog.domain.shared.time.InstantRangeTestSupport.END_AT;
import static com.lilamaris.capstone.academiccatalog.domain.shared.time.InstantRangeTestSupport.START_AT;
import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SimpleInstantRange 테스트")
public class SimpleInstantRangeTest extends AbstractInstantRangeTest<SimpleInstantRange> {
    @Override
    public SimpleInstantRange create(Instant startAt, Instant endAt) {
        return SimpleInstantRange.of(startAt, endAt);
    }

    @Test
    @DisplayName("InstantRange로부터 복사할 수 있다")
    void copy_from_temporal_range() {
        var range = SimpleInstantRange.from(create(START_AT, END_AT));

        assertThat(range.start()).isEqualTo(START_AT);
        assertThat(range.end()).isEqualTo(END_AT);

        assertThatDomainThrownBy(() -> SimpleInstantRange.from(null))
                .hasNonNullMessageFor("range");
    }
}
