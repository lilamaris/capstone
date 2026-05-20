package com.lilamaris.capstone.academiccatalog.domain.shared.time;

import com.lilamaris.capstone.kernel.core.condition.Preconditions;

import java.time.Duration;

public interface TemporalRange<BASE extends Comparable<? super BASE>> {
    BASE start();

    BASE end();

    static <BASE extends Comparable<BASE>> void validate(BASE start, BASE end) {
        Preconditions.requireNonNull(start, "start");
        Preconditions.requireNonNull(end, "end");

        if (end.compareTo(start) <= 0)
            throw new IllegalArgumentException("start must be before end.");
    }

    Duration duration();
}
