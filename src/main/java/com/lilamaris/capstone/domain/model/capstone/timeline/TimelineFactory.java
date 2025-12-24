package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.id.IdGenerator;
import com.lilamaris.capstone.domain.model.common.id.IdSpec;
import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import com.lilamaris.capstone.domain.model.common.id.impl.DefaultIdGenerateContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TimelineFactory {
    private final IdGenerationContext idGenerationContext;

    public TimelineFactory(
            IdGenerator idGenerator,
            RawGenerator<UUID> uuidRawGenerator
    ) {
        Map<IdSpec<?, ?>, RawGenerator<?>> map = Map.of(
                SnapshotId.SPEC, uuidRawGenerator,
                SnapshotLinkId.SPEC, uuidRawGenerator,
                TimelineId.SPEC, uuidRawGenerator
        );

        this.idGenerationContext = new DefaultIdGenerateContext(idGenerator, map);
    }

    public Timeline create(String description) {
        var id = idGenerationContext.next(TimelineId.SPEC);
        return new Timeline(idGenerationContext, id, new ArrayList<>(), new ArrayList<>(), description);
    }
}
