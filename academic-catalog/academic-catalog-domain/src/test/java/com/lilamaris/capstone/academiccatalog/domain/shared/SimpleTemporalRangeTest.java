package com.lilamaris.capstone.academiccatalog.domain.shared;

import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

@DisplayName("SimpleTemporalRange 테스트")
public class SimpleTemporalRangeTest extends AbstractTemporalRangeTest<SimpleTemporalRange> {
    @Override
    public SimpleTemporalRange create(Instant startAt, Instant endAt) {
        return SimpleTemporalRange.of(startAt, endAt);
    }
}
