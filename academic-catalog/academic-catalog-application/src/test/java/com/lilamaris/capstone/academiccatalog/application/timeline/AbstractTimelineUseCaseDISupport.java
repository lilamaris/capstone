package com.lilamaris.capstone.academiccatalog.application.timeline;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineReader;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineStore;
import com.lilamaris.capstone.kernel.testsupport.FixedClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractTimelineUseCaseDISupport<T> {
    @Mock
    protected TimelineReader reader;

    @Mock
    protected TimelineStore store;

    protected Clock clock;

    protected Instant now;

    protected T useCase;

    protected abstract T init(TimelineReader reader, TimelineStore store);

    @BeforeEach
    void run() {
        clock = FixedClock.getFixed();
        now = clock.instant();
        useCase = init(reader, store);
    }
}
