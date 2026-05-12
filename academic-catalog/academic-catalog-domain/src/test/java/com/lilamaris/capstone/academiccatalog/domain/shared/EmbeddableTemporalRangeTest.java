package com.lilamaris.capstone.academiccatalog.domain.shared;

import java.time.Instant;

public class EmbeddableTemporalRangeTest extends AbstractTemporalRangeTest<EmbeddableTemporalRange> {
    @Override
    public EmbeddableTemporalRange create(Instant startAt, Instant endAt) {
        return EmbeddableTemporalRange.of(startAt, endAt);
    }
}
