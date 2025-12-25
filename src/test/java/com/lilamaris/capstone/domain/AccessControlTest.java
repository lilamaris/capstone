package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.domain.model.auth.access_control.AccessControlFactory;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.capstone.timeline.TimelineFactory;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.application.util.generator.DefaultIdGenerationContext;
import com.lilamaris.capstone.util.SequentialUuidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccessControlTest {
    private static final Logger log = LoggerFactory.getLogger(AccessControlTest.class);
    private AccessControlFactory factory;
    private TimelineFactory timelineFactory;
    private UserId userId1;
    private UserId userId2;

    @BeforeEach
    void run() {
        Map<Class<?>, DefaultIdGenerationContext.Binding<?, ?>> accessControlIdMap = Map.of(
                AccessControlId.class, DefaultIdGenerationContext.bind(AccessControlId::new, new SequentialUuidGenerator())
        );

        Map<Class<?>, DefaultIdGenerationContext.Binding<?, ?>> timelineIdMap = Map.of(
                TimelineId.class, DefaultIdGenerationContext.bind(TimelineId::new, new SequentialUuidGenerator()),
                SnapshotId.class, DefaultIdGenerationContext.bind(SnapshotId::new, new SequentialUuidGenerator()),
                SnapshotLinkId.class, DefaultIdGenerationContext.bind(SnapshotLinkId::new, new SequentialUuidGenerator())
        );


        var ctx = new DefaultIdGenerationContext(accessControlIdMap);

        var timelineIdCtx = new DefaultIdGenerationContext(timelineIdMap);

        factory = new AccessControlFactory(ctx);
        timelineFactory = new TimelineFactory(timelineIdCtx);

        var userUuidRawGen = new SequentialUuidGenerator();
        userId1 = new UserId(userUuidRawGen.generate());
        userId2 = new UserId(userUuidRawGen.generate());
    }

    @Test
    void should_create() {
        var referenceable = timelineFactory.create("Test referenceable domain");
        var ref = referenceable.ref();
        var ac1 = factory.create(userId1, ref, "test");

        log.info("AccessControl: {}", ac1);
        assertThat(ac1.getUserId()).isEqualTo(userId1);
        assertThat(ac1.getResourceRef()).isEqualTo(ref);
    }
}
