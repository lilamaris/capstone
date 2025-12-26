package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.application.util.generator.DefaultIdGenerationContext;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Component
public class TimelineFactory {
    private final IdGenerationContext idGenerationContext;

    public TimelineFactory(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        Map<Class<?>, DefaultIdGenerationContext.Binding<?, ?>> map = Map.of(
                SnapshotId.class, DefaultIdGenerationContext.bind(SnapshotId::new, uuidRawGenerator),
                SnapshotLinkId.class, DefaultIdGenerationContext.bind(SnapshotLinkId::new, uuidRawGenerator),
                TimelineId.class, DefaultIdGenerationContext.bind(TimelineId::new, uuidRawGenerator)
        );

        this.idGenerationContext = new DefaultIdGenerationContext(map);
    }

    public Timeline create(String description) {
        var id = idGenerationContext.next(TimelineId.class);
        return new Timeline(idGenerationContext, id, new ArrayList<>(), new ArrayList<>(), description);
    }
}
