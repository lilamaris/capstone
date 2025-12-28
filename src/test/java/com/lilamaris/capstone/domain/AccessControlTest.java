package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.policy.identity.defaults.DefaultIdGenerationContext;
import com.lilamaris.capstone.domain.model.auth.access_control.AccessControl;
import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.domain.contract.Referenceable;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.UserActor;
import com.lilamaris.capstone.util.SequentialUuidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccessControlTest {
    private static final Logger log = LoggerFactory.getLogger(AccessControlTest.class);
    private CanonicalActor actor;
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

        actor = UserActor.of(userIdSupplier.get());
        referenceable = timelineIdSupplier.get();
    }

    @Test
    void should_create() {
        var ref = referenceable.ref();
        var accessControl = AccessControl.create(actor, ref, "test", accessControlIdSupplier);

        log.info("AccessControl: {}", accessControl);
        assertThat(accessControl.getActor().sameIdentity(actor)).isTrue();
        assertThat(accessControl.getResource().sameIdentity(ref));
    }
}
