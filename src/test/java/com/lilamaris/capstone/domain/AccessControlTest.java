package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.policy.identity.defaults.DefaultIdGenerationContext;
import com.lilamaris.capstone.application.policy.identity.defaults.RawBasedIdGenerator;
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

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccessControlTest {
    private static final Logger log = LoggerFactory.getLogger(AccessControlTest.class);
    private CanonicalActor actor;
    private Referenceable referenceable;
    private IdGenerationContext ids;

    @BeforeEach
    void run() {
        var timelineIdGen = new RawBasedIdGenerator<>(TimelineId.class, TimelineId::new, new SequentialUuidGenerator());
        var accessControlIdGen = new RawBasedIdGenerator<>(AccessControlId.class, AccessControlId::new, new SequentialUuidGenerator());
        var userIdGen = new RawBasedIdGenerator<>(UserId.class, UserId::new, new SequentialUuidGenerator());

        ids = new DefaultIdGenerationContext(List.of(timelineIdGen, accessControlIdGen, userIdGen));

        actor = UserActor.of(ids.next(UserId.class).get());
        referenceable = ids.next(TimelineId.class).get();
    }

    @Test
    void should_create() {
        var ref = referenceable.ref();
        var accessControl = AccessControl.create(actor, ref, "test", ids.next(AccessControlId.class));

        log.info("AccessControl: {}", accessControl);
        assertThat(accessControl.getActor().sameIdentity(actor)).isTrue();
        assertThat(accessControl.getResource().sameIdentity(ref));
    }
}
