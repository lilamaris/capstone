package com.lilamaris.capstone.academiccatalog.application.timeline;

import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineReader;
import com.lilamaris.capstone.academiccatalog.application.timeline.port.out.TimelineStore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractTimelineServiceTestDI<T> {
    @Mock
    TimelineReader reader;

    @Mock
    TimelineStore store;
}
