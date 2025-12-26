package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.application.util.generator.DefaultIdGenerationContext;
import com.lilamaris.capstone.domain.model.auth.access_control.AccessControl;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.mixin.Referenceable;
import com.lilamaris.capstone.util.SequentialUuidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccessControlTest {
    private static final Logger log = LoggerFactory.getLogger(AccessControlTest.class);
    private UserId userId1;
    private UserId userId2;
    private Referenceable referenceable;

    private IdGenerationContext ids;
    private Supplier<AccessControlId> accessControlIdSupplier;
    private Supplier<TimelineId> timelineIdSupplier;
    private Supplier<UserId> userIdSupplier;


    @BeforeEach
    void run() {
        var registry = new DefaultIdGenerationContext();
        var uuidRawGenerator = new SequentialUuidGenerator();
        registry.register(AccessControlId.class, AccessControlId::new, uuidRawGenerator);
        registry.register(TimelineId.class, TimelineId::new, uuidRawGenerator);
        registry.register(UserId.class, UserId::new, uuidRawGenerator);

        ids = registry;
        accessControlIdSupplier = () -> ids.next(AccessControlId.class);
        timelineIdSupplier = () -> ids.next(TimelineId.class);
        userIdSupplier = () -> ids.next(UserId.class);

        userId1 = userIdSupplier.get();
        userId2 = userIdSupplier.get();
        referenceable = timelineIdSupplier.get();
    }

    @Test
    void should_create() {
        var ref = referenceable.ref();
        var accessControl = AccessControl.create(userId1, ref, "test", accessControlIdSupplier);

        log.info("AccessControl: {}", accessControl);
        assertThat(accessControl.getUserId()).isEqualTo(userId1);
        assertThat(accessControl.getResourceRef()).isEqualTo(ref);
    }
}
